package org.napile.idea.plugin.editor.hierarchy.tree;

import java.util.ArrayList;
import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.napile.compiler.analyzer.AnalyzeExhaust;
import org.napile.compiler.lang.descriptors.ClassDescriptor;
import org.napile.compiler.lang.descriptors.ClassifierDescriptor;
import org.napile.compiler.lang.psi.NapileClass;
import org.napile.compiler.lang.resolve.BindingTraceKeys;
import org.napile.compiler.lang.resolve.BindingTraceUtil;
import org.napile.compiler.lang.types.NapileType;
import org.napile.idea.plugin.module.ModuleAnalyzerUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;

/**
 * @author VISTALL
 * @since 9:19/19.04.13
 */
public class NapileTypeHierarchyTreeStructure extends NapileSubtypesHierarchyTreeStructure
{
	public NapileTypeHierarchyTreeStructure(Project myProject, NapileClass napileClass)
	{
		super(myProject, napileClass);
	}

	@NotNull
	@Override
	protected NapileTypeHierarchyNodeDescriptor buildMe(NapileClass napileClass)
	{
		NapileTypeHierarchyNodeDescriptor parent = null;

		final AnalyzeExhaust analyzeExhaust = ModuleAnalyzerUtil.lastAnalyze(napileClass.getContainingFile());

		final ClassDescriptor classDescriptor = analyzeExhaust.getBindingTrace().get(BindingTraceKeys.CLASS, napileClass);
		if(classDescriptor != null)
		{
			final Collection<NapileType> supertypes = ContainerUtil.reverse(new ArrayList<NapileType>(classDescriptor.getSupertypes()));  //FIXME [VISTALL] better?

			for(NapileType type : supertypes)
			{
				final ClassifierDescriptor declarationDescriptor = type.getConstructor().getDeclarationDescriptor();
				if(!(declarationDescriptor instanceof ClassDescriptor))
				{
					continue;
				}

				final PsiElement element = BindingTraceUtil.descriptorToDeclaration(analyzeExhaust.getBindingTrace(), declarationDescriptor);
				if(element instanceof NapileClass && element != napileClass)
				{
					NapileTypeHierarchyNodeDescriptor newDescriptor = new NapileTypeHierarchyNodeDescriptor(element.getProject(), parent, (NapileClass) element, false);
					if(parent != null)
					{
						parent.setCachedChildren(new Object[]{newDescriptor});
					}
					parent = newDescriptor;
				}
			}
		}

		final NapileTypeHierarchyNodeDescriptor thisNode = new NapileTypeHierarchyNodeDescriptor(napileClass.getProject(), parent, napileClass, true);
		if(parent != null)
		{
			parent.setCachedChildren(new Object[]{thisNode});
		}
		return thisNode;
	}
}
