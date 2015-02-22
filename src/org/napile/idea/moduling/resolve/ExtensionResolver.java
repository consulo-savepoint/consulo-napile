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

package org.napile.idea.moduling.resolve;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.idea.moduling.dom.ExtensionPoint;
import org.napile.idea.moduling.dom.ExtensionPoints;
import org.napile.idea.moduling.dom.Module;
import com.intellij.openapi.module.ModuleServiceManager;
import com.intellij.openapi.util.Comparing;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;

/**
 * @author VISTALL
 * @since 16:46/21.08.12
 */
public class ExtensionResolver
{
	@NotNull
	public static ExtensionResolver getInstance(com.intellij.openapi.module.Module module)
	{
		return ModuleServiceManager.getService(module, ExtensionResolver.class);
	}

	private final com.intellij.openapi.module.Module module;

	public ExtensionResolver(com.intellij.openapi.module.Module module)
	{
		this.module = module;
	}

	@Nullable
	public Module findModule(String name)
	{
		if(name == null)
			return null;

		List<Module> modules = getVisibleModules();
		for(Module m : modules)
		{
			String id = m.getId().getStringValue();
			if(name.equals(id))
				return m;
		}
		return null;
	}

	public List<ExtensionPoint> findPoints(@NotNull String id)
	{
		List<ExtensionPoint> result = new ArrayList<ExtensionPoint>();
		for(Module module : getVisibleModules())
		{
			if(!Comparing.equal(id, module.getId().getStringValue()))
				continue;

			for(ExtensionPoints extensionPoints : module.getExtensionPoints())
				result.addAll(extensionPoints.getExtensionPoints());
		}
		return result;
	}

	@NotNull
	public List<Module> getVisibleModules()
	{
		List<DomFileElement<Module>> elements = DomService.getInstance().getFileElements(Module.class, module.getProject(), GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module));
		List<Module> modules = new ArrayList<Module>(elements.size());
		for(DomFileElement<Module> element : elements)
			modules.add(element.getRootElement());
		return modules;
	}
}
