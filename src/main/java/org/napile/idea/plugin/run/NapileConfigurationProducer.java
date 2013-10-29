/*
 * Copyright 2010-2013 napile.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.napile.idea.plugin.run;

import org.napile.asm.resolve.name.FqName;
import org.napile.compiler.analyzer.AnalyzeExhaust;
import org.napile.compiler.lang.descriptors.MutableClassDescriptor;
import org.napile.compiler.lang.descriptors.SimpleMethodDescriptor;
import org.napile.compiler.lang.psi.NapileClass;
import org.napile.compiler.lang.psi.NapileDeclaration;
import org.napile.compiler.lang.psi.NapileFile;
import org.napile.compiler.lang.resolve.BindingTraceKeys;
import org.napile.compiler.lang.resolve.DescriptorUtils;
import org.napile.compiler.util.RunUtil;
import org.napile.idea.plugin.module.ModuleAnalyzerUtil;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * @author VISTALL
 * @since 13:22/08.01.13
 */
public class NapileConfigurationProducer extends RunConfigurationProducer<NapileRunConfiguration>
{
	public NapileConfigurationProducer()
	{
		super(NapileConfigurationType.getInstance());
	}

	@Override
	protected boolean setupConfigurationFromContext(NapileRunConfiguration runConfiguration, ConfigurationContext configurationContext, Ref<PsiElement> psiElementRef)
	{
		NapileClass napileClass = findClass(psiElementRef.get());
		if(napileClass == null)
		{
			return false;
		}

		AnalyzeExhaust analyzeExhaust = ModuleAnalyzerUtil.lastAnalyze(napileClass.getContainingFile());

		MutableClassDescriptor descriptor = (MutableClassDescriptor) analyzeExhaust.getBindingTrace().get(BindingTraceKeys.CLASS, napileClass);
		if(descriptor == null)
		{
			return false;
		}

		NapileDeclaration mainMethod = null;
		for(NapileDeclaration inner : napileClass.getDeclarations())
		{
			SimpleMethodDescriptor methodDescriptor = analyzeExhaust.getBindingTrace().get(BindingTraceKeys.METHOD, inner);
			if(methodDescriptor != null && RunUtil.isRunPoint(methodDescriptor))
			{
				mainMethod = inner;
				break;
			}
		}

		if(mainMethod != null)
		{
			Module module = mainMethod.isValid() ? ModuleUtilCore.findModuleForPsiElement(mainMethod) : null;
			if(module == null)
			{
				return false;
			}

			FqName fqName = DescriptorUtils.getFQName(descriptor).toSafe();

			runConfiguration.setModule(module);
			runConfiguration.setName(fqName.getFqName());
			runConfiguration.mainClass = fqName.getFqName();

			psiElementRef.set(napileClass);
			return true;
		}

		return false;
	}

	@Override
	public boolean isConfigurationFromContext(NapileRunConfiguration appConfiguration, ConfigurationContext context)
	{
		NapileClass psiLocation = findClass(context.getPsiLocation());
		if(psiLocation != null)
		{
			if(psiLocation.getFqName().toString().equals(appConfiguration.mainClass))
			{
				final Module configurationModule = appConfiguration.getConfigurationModule().getModule();
				if(Comparing.equal(context.getModule(), configurationModule))
				{
					return true;
				}

				NapileRunConfiguration template = (NapileRunConfiguration) context.getRunManager().getConfigurationTemplate(getConfigurationFactory()).getConfiguration();
				final Module predefinedModule = template.getConfigurationModule().getModule();
				if(Comparing.equal(predefinedModule, configurationModule))
				{
					return true;
				}
			}
		}

		return false;
	}

	private NapileClass findClass(PsiElement element)
	{
		if(element == null)
		{
			return null;
		}
		NapileClass napileClass;
		if(element instanceof NapileFile)
		{
			if(((NapileFile) element).getDeclarations().length == 0)
			{
				return null;
			}

			napileClass = ((NapileFile) element).getDeclarations()[0];
		}
		else if(element instanceof NapileClass)
		{
			return (NapileClass) element;
		}
		else
		{
			NapileClass clazz = PsiTreeUtil.getParentOfType(element, NapileClass.class);
			if(clazz == null)
			{
				return null;
			}
			napileClass = clazz;
		}

		return napileClass;
	}
}
