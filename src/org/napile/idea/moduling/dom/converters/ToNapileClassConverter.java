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

import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.ResolvingConverter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.compiler.lang.psi.NapileClass;
import org.napile.compiler.lang.psi.NapileClassLike;
import org.napile.idea.moduling.resolve.NapileResolver;
import org.napile.idea.plugin.stubindex.NapileFullClassNameIndex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 10:40/26.01.13
 */
public class ToNapileClassConverter extends ResolvingConverter<NapileClassLike>
{
	@NotNull
	@Override
	public Collection<? extends NapileClassLike> getVariants(ConvertContext context)
	{
		Collection<String> keys = NapileFullClassNameIndex.getInstance().getAllKeys(context.getProject());
		List<NapileClassLike> list = new ArrayList<NapileClassLike>(keys.size());
		for(String fqName : NapileFullClassNameIndex.getInstance().getAllKeys(context.getProject()))
		{
			Collection<NapileClass> classes = NapileFullClassNameIndex.getInstance().get(fqName, context.getProject(), GlobalSearchScope.allScope(context.getProject()));
			list.addAll(classes);
		}
		return list;
	}

	@Nullable
	@Override
	public NapileClassLike fromString(@Nullable @NonNls String s, ConvertContext context)
	{
		if(s == null || context.getModule() == null)
			return null;

		return NapileResolver.findNapileClass(s, context.getProject(), context.getModule().getModuleWithDependenciesScope());
	}

	@Nullable
	@Override
	public String toString(@Nullable NapileClassLike napileClass, ConvertContext context)
	{
		if(napileClass == null)
			return null;
		return napileClass.getFqName().getFqName();
	}
}
