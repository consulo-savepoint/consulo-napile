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

package org.napile.idea.plugin.module;

import org.jetbrains.annotations.NotNull;
import org.napile.compiler.analyzer.AnalyzeExhaust;
import org.napile.compiler.lang.descriptors.DeclarationDescriptor;
import org.napile.compiler.lang.psi.NapileElement;
import org.napile.compiler.lang.psi.NapileFile;
import org.napile.compiler.lang.resolve.BindingTraceKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.libraries.LibraryUtil;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author VISTALL
 * @since 15:31/11.01.13
 */
public class ModuleAnalyzerUtil
{
	@NotNull
	public static AnalyzeExhaust lastAnalyze(@NotNull final NapileFile file)
	{
		return analyzeOrGet(file, false);
	}

	/**
	 * Return new AnalyzeExhaust
	 * @param file
	 * @return
	 */
	@NotNull
	public static AnalyzeExhaust analyze(@NotNull final NapileFile file)
	{
		return analyzeOrGet(file, true);
	}

	public static <T extends DeclarationDescriptor> T getDescriptorOrAnalyze(@NotNull NapileElement napileElement)
	{
		AnalyzeExhaust analyzeExhaust = lastAnalyze(napileElement.getContainingFile());

		DeclarationDescriptor declarationDescriptor = analyzeExhaust.getBindingTrace().get(BindingTraceKeys.DECLARATION_TO_DESCRIPTOR, napileElement);

		return (T) declarationDescriptor;
	}

	@NotNull
	private static AnalyzeExhaust analyzeOrGet(@NotNull final NapileFile file, boolean updateIfNeed)
	{
		Module module = ModuleUtilCore.findModuleForPsiElement(file);
		if(module == null)
		{
			final OrderEntry libraryEntry = LibraryUtil.findLibraryEntry(file.getVirtualFile(), file.getProject());
			if(libraryEntry != null)
			{
				module = libraryEntry.getOwnerModule();
			}
		}

		if(module == null)
		{
			return ModuleAnalyzer.EMPTY;
		}

		ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);

		final VirtualFile virtualFile = file.getVirtualFile();
		if(virtualFile == null)
		{
			return ModuleAnalyzer.EMPTY;
		}
		final boolean test = moduleRootManager.getFileIndex().isInTestSourceContent(virtualFile);

		final ModuleAnalyzer instance = ModuleAnalyzer.getInstance(module);

		return test ? instance.getTestSourceAnalyze(updateIfNeed) : instance.getSourceAnalyze(updateIfNeed);
	}
}
