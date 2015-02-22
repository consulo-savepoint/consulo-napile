/*
 * Copyright 2010-2012 napile.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.napile.idea.moduling.dom.impl;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.resolve.name.FqName;
import org.napile.compiler.analyzer.AnalyzeExhaust;
import org.napile.compiler.lang.descriptors.CallParameterDescriptor;
import org.napile.compiler.lang.descriptors.ConstructorDescriptor;
import org.napile.compiler.lang.descriptors.VariableDescriptor;
import org.napile.compiler.lang.descriptors.annotations.AnnotationDescriptor;
import org.napile.compiler.lang.lexer.NapileNodes;
import org.napile.compiler.lang.psi.NapileClassLike;
import org.napile.compiler.lang.psi.NapileDeclaration;
import org.napile.compiler.lang.psi.NapileExpression;
import org.napile.compiler.lang.psi.NapileValueArgument;
import org.napile.compiler.lang.resolve.BindingTrace;
import org.napile.compiler.lang.resolve.BindingTraceKeys;
import org.napile.compiler.lang.resolve.calls.ExpressionValueArgument;
import org.napile.compiler.lang.resolve.calls.ResolvedCall;
import org.napile.compiler.lang.resolve.calls.ResolvedValueArgument;
import org.napile.compiler.lang.types.NapileType;
import org.napile.compiler.lang.types.TypeUtils;
import org.napile.idea.moduling.dom.Extension;
import org.napile.idea.moduling.dom.ExtensionPoint;
import org.napile.idea.moduling.dom.Extensions;
import org.napile.idea.moduling.dom.Module;
import org.napile.idea.moduling.dom.RefAttributeToTypeParameter;
import org.napile.idea.moduling.dom.converters.ToNapileClassConverter;
import org.napile.idea.moduling.resolve.ExtensionResolver;
import org.napile.idea.plugin.module.ModuleAnalyzerUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.XmlName;
import com.intellij.util.xml.reflect.DomExtender;
import com.intellij.util.xml.reflect.DomExtension;
import com.intellij.util.xml.reflect.DomExtensionsRegistrar;

/**
 * @author VISTALL
 * @since 2:17/18.08.12
 */
public class ExtensionDomExtender extends DomExtender<Extensions>
{
	private static final FqName DOM_ATTRIBUTE = new FqName("xml.annotation.DomAttribute");
	private static final ToNapileClassConverter CLASS_CONVERTER = new ToNapileClassConverter();

	@Override
	public void registerExtensions(@NotNull Extensions extensions, @NotNull DomExtensionsRegistrar registrar)
	{
		Module module = extensions.getParentOfType(Module.class, true);
		if(module == null)
			return;

		String id = extensions.getForId().getStringValue();
		if(id == null)
			return;

		for(ExtensionPoint point : ExtensionResolver.getInstance(extensions.getModule()).findPoints(id))
		{
			registerExtensionPoint(registrar, point);
		}
	}

	private void registerExtensionPoint(DomExtensionsRegistrar registrar, final ExtensionPoint point)
	{
		String pointName = point.getName().getStringValue();
		if(pointName == null)
			return;

		final NapileClassLike napileClassLike = point.getDescriptor().getValue();
		if(napileClassLike == null)
			return;

		AnalyzeExhaust analyzeExhaust = ModuleAnalyzerUtil.lastAnalyze(napileClassLike.getContainingFile());
		final BindingTrace bindingTrace = analyzeExhaust.getBindingTrace();

		final DomExtension domExtension = registrar.registerCollectionChildrenExtension(new XmlName(pointName), Extension.class);
		domExtension.setDeclaringElement(point);
		domExtension.addExtender(new DomExtender()
		{
			@Override
			public void registerExtensions(@NotNull DomElement domElement, @NotNull DomExtensionsRegistrar registrar)
			{
				for(NapileDeclaration declaration : napileClassLike.getDeclarations())
				{
					VariableDescriptor variableDescriptor = bindingTrace.get(BindingTraceKeys.VARIABLE, declaration);
					if(variableDescriptor == null)
						continue;

					String value = getAnnotationValue(variableDescriptor, "name");
					if(value == null)
						continue;

					DomExtension extension = registrar.registerGenericAttributeValueChildExtension(new XmlName(value), String.class);
					extension.setDeclaringElement(declaration);
				}

				for(RefAttributeToTypeParameter refAttributeToTypeParameter : point.getRefAttributeToTypeParameterList())
				{
					String attName = refAttributeToTypeParameter.getAttribute().getValue();
					if(attName == null)
						continue;

					DomExtension extension = registrar.registerGenericAttributeValueChildExtension(new XmlName(attName), NapileClassLike.class);
					extension.setDeclaringElement(refAttributeToTypeParameter);
					extension.setConverter(CLASS_CONVERTER);
				}
			}
		});
	}

	private static String getAnnotationValue(VariableDescriptor variableDescriptor, String name)
	{
		AnnotationDescriptor annotationDescriptor = null;
		for(AnnotationDescriptor a : variableDescriptor.getAnnotations())
		{
			NapileType type = a.getType();
			if(type == null || !TypeUtils.isEqualFqName(type, DOM_ATTRIBUTE))
				continue;

			annotationDescriptor = a;
			break;
		}

		if(annotationDescriptor == null)
			return null;

		ResolvedCall<ConstructorDescriptor> resolvedCall = annotationDescriptor.getResolvedCall();
		if(resolvedCall == null)
			return null;

		for(Map.Entry<CallParameterDescriptor, ResolvedValueArgument> entry : resolvedCall.getValueArguments().entrySet())
		{
			if(!entry.getKey().getName().getIdentifier().equals(name))
				continue;

			ResolvedValueArgument value = entry.getValue();
			if(value instanceof ExpressionValueArgument)
			{
				NapileValueArgument valueArgument = (NapileValueArgument) ((ExpressionValueArgument) value).getValueArgument();
				if(valueArgument == null)
					continue;

				NapileExpression valueExpression = valueArgument.getArgumentExpression();
				if(valueExpression != null && valueExpression.getNode().getElementType() == NapileNodes.STRING_CONSTANT)
				{
					return StringUtil.unquoteString(valueExpression.getText());
				}
			}
		}
		return null;
	}
}
