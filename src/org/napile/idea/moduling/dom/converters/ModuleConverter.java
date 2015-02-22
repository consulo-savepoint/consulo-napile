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

package org.napile.idea.moduling.dom.converters;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.idea.moduling.dom.Module;
import org.napile.idea.moduling.resolve.ExtensionResolver;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.ResolvingConverter;

/**
 * @author VISTALL
 * @since 16:51/21.08.12
 */
public class ModuleConverter extends ResolvingConverter<Module>
{
	@NotNull
	@Override
	public Collection<? extends Module> getVariants(ConvertContext convertContext)
	{
		return ExtensionResolver.getInstance(convertContext.getModule()).getVisibleModules();
	}

	@Nullable
	@Override
	public Module fromString(@Nullable @NonNls String s, ConvertContext convertContext)
	{
		ExtensionResolver extensionResolver = ExtensionResolver.getInstance(convertContext.getModule());

		return extensionResolver.findModule(s);
	}

	@Nullable
	@Override
	public String toString(@Nullable Module module, ConvertContext convertContext)
	{
		return module == null ? StringUtils.EMPTY : module.getId().getStringValue();
	}
}
