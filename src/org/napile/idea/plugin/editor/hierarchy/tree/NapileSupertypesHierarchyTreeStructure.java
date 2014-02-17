package org.napile.idea.plugin.editor.hierarchy.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.napile.compiler.analyzer.AnalyzeExhaust;
import org.napile.compiler.lang.descriptors.ClassDescriptor;
import org.napile.compiler.lang.descriptors.ClassifierDescriptor;
import org.napile.compiler.lang.psi.NapileClass;
import org.napile.compiler.lang.resolve.BindingTraceKeys;
import org.napile.compiler.lang.resolve.BindingTraceUtil;
import org.napile.compiler.lang.types.NapileType;
import org.napile.idea.plugin.module.ModuleAnalyzerUtil;
import com.intellij.ide.hierarchy.HierarchyNodeDescriptor;
import com.intellij.ide.hierarchy.HierarchyTreeStructure;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @since 9:02/19.04.13
 */
public class NapileSupertypesHierarchyTreeStructure extends HierarchyTreeStructure
{
	public NapileSupertypesHierarchyTreeStructure(Project project, NapileClass psiElement)
	{
		super(project, new NapileTypeHierarchyNodeDescriptor(project, null, psiElement, true));
	}

	@Override
	protected Object[] buildChildren(HierarchyNodeDescriptor descriptor)
	{
		NapileTypeHierarchyNodeDescriptor napileTypeHierarchyNodeDescriptor = (NapileTypeHierarchyNodeDescriptor) descriptor;

		NapileClass napileClass = napileTypeHierarchyNodeDescriptor.getPsiClass();

		final AnalyzeExhaust analyzeExhaust = ModuleAnalyzerUtil.lastAnalyze(napileClass.getContainingFile());

		final ClassDescriptor classDescriptor = analyzeExhaust.getBindingTrace().get(BindingTraceKeys.CLASS, napileClass);
		if(classDescriptor == null)
		{
			return ArrayUtil.EMPTY_OBJECT_ARRAY;
		}

		final Collection<NapileType> supertypes = classDescriptor.getSupertypes();
		final List<NapileTypeHierarchyNodeDescriptor> nodes = new ArrayList<NapileTypeHierarchyNodeDescriptor>(supertypes.size());
		for(NapileType type : supertypes)
		{
			final ClassifierDescriptor declarationDescriptor = type.getConstructor().getDeclarationDescriptor();
			if(!(declarationDescriptor instanceof ClassDescriptor))
			{
				continue;
			}

			final PsiElement element = BindingTraceUtil.descriptorToDeclaration(analyzeExhaust.getBindingTrace(), declarationDescriptor);
			if(element != null && element != napileClass)
			{
				nodes.add(new NapileTypeHierarchyNodeDescriptor(myProject, descriptor, element, false));
			}
		}
		return ArrayUtil.toObjectArray(nodes);
	}
}
