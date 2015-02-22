/*
 * Copyright 2010-2013 napile.org
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

package org.napile.idea.moduling.dom.converters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.compiler.lang.psi.NapileClass;
import org.napile.compiler.lang.psi.NapileTypeParameter;
import org.napile.idea.moduling.dom.ExtensionPoint;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.DomUtil;
import com.intellij.util.xml.ResolvingConverter;

/**
 * @author VISTALL
 * @since 10:54/26.01.13
 */
public class ToTypeParameterConverter extends ResolvingConverter<NapileTypeParameter>
{
	@NotNull
	@Override
	public Collection<? extends NapileTypeParameter> getVariants(ConvertContext context)
	{
		NapileClass napileClass = findNapileClass(context);
		if(napileClass == null)
			return Collections.emptyList();

		return Arrays.asList(napileClass.getTypeParameters());
	}

	@Nullable
	@Override
	public NapileTypeParameter fromString(@Nullable @NonNls String s, ConvertContext context)
	{
		if(s == null)
			return null;
		NapileClass napileClass = findNapileClass(context);
		if(napileClass == null)
			return null;
		for(NapileTypeParameter typeParameter : napileClass.getTypeParameters())
		{
			if(s.equals(typeParameter.getName()))
			{
				return typeParameter;
			}
		}
		return null;
	}

	@Nullable
	@Override
	public String toString(@Nullable NapileTypeParameter napileTypeParameter, ConvertContext context)
	{
		return napileTypeParameter == null ? null : napileTypeParameter.getName();
	}

	private static NapileClass findNapileClass(ConvertContext convertContext)
	{
		ExtensionPoint extensionPoint = DomUtil.getParentOfType(convertContext.getInvocationElement(), ExtensionPoint.class, false);

		assert extensionPoint != null;

		return (NapileClass) extensionPoint.getDescriptor().getValue();
	}
}
