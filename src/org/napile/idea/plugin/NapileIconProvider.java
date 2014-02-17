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

package org.napile.idea.plugin;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IconDescriptor;
import com.intellij.ide.IconDescriptorUpdater;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import com.intellij.util.BitUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.asm.lib.NapileAnnotationPackage;
import org.napile.asm.lib.NapileLangPackage;
import org.napile.compiler.lang.descriptors.MethodDescriptor;
import org.napile.compiler.lang.descriptors.MutableClassDescriptor;
import org.napile.compiler.lang.lexer.NapileTokens;
import org.napile.compiler.lang.psi.*;
import org.napile.compiler.lang.resolve.AnnotationUtils;
import org.napile.compiler.lang.resolve.DescriptorUtils;
import org.napile.compiler.util.RunUtil;
import org.napile.idea.plugin.module.ModuleAnalyzerUtil;

import javax.swing.*;

/**
 * @author yole
 */
public class NapileIconProvider implements IconDescriptorUpdater
{
	public static final Icon FINAL_MARK_ICON = IconLoader.getIcon("/nodes/finalMark.png");
	public static final Icon RUNNABLE_MARK = IconLoader.getIcon("/nodes/runnableMark.png");

	public static NapileIconProvider INSTANCE = new NapileIconProvider();

	@Override
	public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int flags) {
		Icon icon = getIcon(element, iconDescriptor, flags);
		if(icon != null) {
			iconDescriptor.setMainIcon(icon);
		}
	}

	private static Icon getIcon(@NotNull PsiElement psiElement, @NotNull IconDescriptor iconDescriptor,@NotNull int flags)
	{
		Icon icon = null;
		boolean isRunnable = false;

		if(psiElement instanceof NapileFile)
		{
			NapileFile file = (NapileFile) psiElement;

			icon = file.getDeclarations().length == 1 ? getIcon(file.getDeclarations()[0], iconDescriptor, flags) : NapileIcons.FILE;
		}
		else if(psiElement instanceof NapileNamedMethodOrMacro)
		{
			icon = NapileIcons.METHOD;
		}
		else if(psiElement instanceof NapileConstructor)
		{
			icon = NapileIcons.CONSTRUCTOR;
		}
		else if(psiElement instanceof NapileTypeParameter)
		{
			icon = NapileIcons.TYPE_PARAMETER;
		}
		else if(psiElement instanceof NapileClass)
		{
			NapileClass napileClass = (NapileClass) psiElement;
			MutableClassDescriptor descriptor = ModuleAnalyzerUtil.getDescriptorOrAnalyze(napileClass);

			icon = napileClass.hasModifier(NapileTokens.ABSTRACT_KEYWORD) ? NapileIcons.ABSTRACT_CLASS : NapileIcons.CLASS;

			if(descriptor != null)
			{
				if(descriptor.isTraited())
					icon = napileClass.hasModifier(NapileTokens.ABSTRACT_KEYWORD) ? NapileIcons.ABSTRACT_CLASS_TRAITED : NapileIcons.CLASS_TRAITED;

				if(napileClass.hasModifier(NapileTokens.UTIL_KEYWORD))
					icon = NapileIcons.UTIL;

				if(AnnotationUtils.isAnnotation(descriptor))
				{
					icon = NapileIcons.ANNOTATION;
					if(AnnotationUtils.hasAnnotation(descriptor, NapileAnnotationPackage.REPEATABLE))
						icon = NapileIcons.REPEATABLE_ANNOTATION;
				}

				if(DescriptorUtils.isSubclassOf(descriptor, NapileLangPackage.EXCEPTION))
					icon = napileClass.hasModifier(NapileTokens.ABSTRACT_KEYWORD) ? NapileIcons.ABSTRACT_THROWABLE : NapileIcons.THROWABLE;

				for(MethodDescriptor m : descriptor.getMethods())
					if(RunUtil.isRunPoint(m))
					{
						isRunnable = true;
						break;
					}
			}
		}
		else if(psiElement instanceof NapileVariable || psiElement instanceof NapileCallParameterAsVariable)
		{
			icon = NapileIcons.VARIABLE;
		}

		modifyIcon(psiElement instanceof NapileModifierListOwner ? ((NapileModifierListOwner) psiElement) : null, iconDescriptor, flags, isRunnable);

		return icon;
	}

	private static void modifyIcon(@Nullable NapileModifierListOwner modifierList, IconDescriptor descriptor, int flags, boolean isRunnable)
	{
		if(modifierList != null)
		{
			if(modifierList.hasModifier(NapileTokens.FINAL_KEYWORD))
			{
				descriptor.addLayerIcon(AllIcons.Nodes.FinalMark);
			}

			if(isRunnable)
			{
				descriptor.addLayerIcon(AllIcons.Nodes.RunnableMark);
			}

			if(BitUtil.isSet(flags, Iconable.ICON_FLAG_VISIBILITY))
			{
				if(modifierList.hasModifier(NapileTokens.LOCAL_KEYWORD))
				{
					descriptor.setRightIcon(AllIcons.Nodes.C_private);
				}
				else if(modifierList.hasModifier(NapileTokens.COVERED_KEYWORD))
				{
					descriptor.setRightIcon(AllIcons.Nodes.C_protected);
				}
				else if(modifierList.hasModifier(NapileTokens.HERITABLE_KEYWORD))
				{
					descriptor.setRightIcon(NapileIcons.C_HERITABLE);
				}
				else
				{
					descriptor.setRightIcon(AllIcons.Nodes.C_public);
				}
			}
		}
	}
}
