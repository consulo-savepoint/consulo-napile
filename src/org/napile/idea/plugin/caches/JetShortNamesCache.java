/*
 * Copyright 2010-2012 JetBrains s.r.o.
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

package org.napile.idea.plugin.caches;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.napile.asm.resolve.name.FqName;
import org.napile.compiler.lang.descriptors.ClassDescriptor;
import org.napile.compiler.lang.descriptors.DeclarationDescriptor;
import org.napile.compiler.lang.resolve.BindingContext;
import org.napile.compiler.lang.resolve.NapileFilesProvider;
import org.napile.compiler.lang.psi.NapileClass;
import org.napile.compiler.lang.psi.NapileClassLike;
import org.napile.compiler.lang.psi.NapileElement;
import org.napile.compiler.lang.psi.NapileFile;
import org.napile.idea.plugin.project.WholeProjectAnalyzerFacade;
import org.napile.idea.plugin.stubindex.NapileFullClassNameIndex;
import org.napile.idea.plugin.stubindex.NapileShortClassNameIndex;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ArrayUtil;

/**
 * All those declaration are planned to be used in completion.
 *
 * @author Nikolay Krasko
 */
public class JetShortNamesCache
{
	private final Project project;

	@NotNull
	public static JetShortNamesCache getInstance(final Project project)
	{
		return ServiceManager.getService(project, JetShortNamesCache.class);
	}

	public JetShortNamesCache(Project project)
	{
		this.project = project;
	}

	@NotNull
	public Map<NapileClassLike, ClassDescriptor> getAllClassesAndDescriptors(@NotNull NapileElement napileElement, @NotNull GlobalSearchScope globalSearchScope)
	{
		BindingContext context = WholeProjectAnalyzerFacade.analyzeProjectWithCacheOnAFile(napileElement.getContainingFile()).getBindingContext();

		NapileFilesProvider jetFilesProvider = NapileFilesProvider.getInstance(project);

		Map<NapileClassLike, ClassDescriptor> result = new HashMap<NapileClassLike, ClassDescriptor>();

		for(NapileFile temp : jetFilesProvider.allInScope(globalSearchScope))
		{
			for(NapileClass napileClass : temp.getDeclarations())
			{
				DeclarationDescriptor declarationDescriptor = context.get(BindingContext.DECLARATION_TO_DESCRIPTOR, napileClass);

				result.put(napileClass, (ClassDescriptor) declarationDescriptor);
			}
		}
		return result;
	}

	/**
	 * Return idea class names form idea project sources which should be visible from java.
	 */
	@NotNull
	public String[] getAllClassNames()
	{
		Collection<String> classNames = NapileShortClassNameIndex.getInstance().getAllKeys(project);
		return ArrayUtil.toStringArray(classNames);
	}

	/**
	 * Return class names form idea sources in given scope which should be visible as Java classes.
	 */
	@NotNull
	public NapileClassLike[] getClassesByName(@NotNull @NonNls String name, @NotNull GlobalSearchScope scope)
	{
		// Quick check for classes from getAllClassNames()
		Collection<NapileClassLike> classOrObjects = NapileShortClassNameIndex.getInstance().get(name, project, scope);
		return classOrObjects.isEmpty() ? NapileClassLike.EMPTY_ARRAY : classOrObjects.toArray(NapileClassLike.EMPTY_ARRAY);
	}

	public Collection<ClassDescriptor> getJetClassesDescriptors(@NotNull Condition<String> acceptedShortNameCondition, @NotNull NapileFile jetFile)
	{
		BindingContext context = WholeProjectAnalyzerFacade.analyzeProjectWithCacheOnAFile(jetFile).getBindingContext();
		Collection<ClassDescriptor> classDescriptors = new ArrayList<ClassDescriptor>();

		for(String fqName : NapileFullClassNameIndex.getInstance().getAllKeys(project))
		{
			FqName classFQName = new FqName(fqName);
			if(acceptedShortNameCondition.value(classFQName.shortName().getName()))
			{
				ClassDescriptor descriptor = context.get(BindingContext.FQNAME_TO_CLASS_DESCRIPTOR, classFQName);
				if(descriptor != null)
				{
					classDescriptors.add(descriptor);
				}
			}
		}

		return classDescriptors;
	}
}
