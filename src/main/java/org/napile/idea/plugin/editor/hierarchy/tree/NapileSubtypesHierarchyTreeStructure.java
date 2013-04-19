package org.napile.idea.plugin.editor.hierarchy.tree;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.compiler.analyzer.AnalyzeExhaust;
import org.napile.compiler.lang.descriptors.ClassDescriptor;
import org.napile.compiler.lang.psi.NapileClass;
import org.napile.compiler.lang.resolve.BindingTraceKeys;
import org.napile.compiler.lang.resolve.BindingTraceUtil;
import org.napile.compiler.lang.resolve.DescriptorUtils;
import org.napile.idea.plugin.module.ModuleAnalyzerUtil;
import com.intellij.ide.hierarchy.HierarchyNodeDescriptor;
import com.intellij.ide.hierarchy.HierarchyTreeStructure;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @since 8:52/19.04.13
 */
public class NapileSubtypesHierarchyTreeStructure extends HierarchyTreeStructure
{
	public NapileSubtypesHierarchyTreeStructure(Project myProject, NapileClass napileClass)
	{
		super(myProject, null);

		myBaseDescriptor = buildMe(napileClass);

		setBaseElement(myBaseDescriptor);
	}

	@NotNull
	protected NapileTypeHierarchyNodeDescriptor buildMe(NapileClass napileClass)
	{
		return new NapileTypeHierarchyNodeDescriptor(napileClass.getProject(), null, napileClass, true);
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

		final List<NapileTypeHierarchyNodeDescriptor> nodes = new ArrayList<NapileTypeHierarchyNodeDescriptor>();
		for(ClassDescriptor temp : analyzeExhaust.getBodiesResolveContext().getClasses().values())
		{
			if(DescriptorUtils.isSubclass(temp, classDescriptor))
			{
				final PsiElement element = BindingTraceUtil.descriptorToDeclaration(analyzeExhaust.getBindingTrace(), temp);
				if(element != null && element != napileClass)
				{
					nodes.add(new NapileTypeHierarchyNodeDescriptor(myProject, descriptor, element, false));
				}
			}
		}

		return ArrayUtil.toObjectArray(nodes);
	}
}
