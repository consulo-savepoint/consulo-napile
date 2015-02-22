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

package org.napile.idea.moduling.projectView.nodes;

import java.util.Collection;

import org.jetbrains.annotations.Nullable;
import org.napile.idea.moduling.dom.ExtensionPoint;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.AbstractPsiBasedNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 16:00/21.08.12
 */
public class ExtensionPointNode extends AbstractPsiBasedNode<ExtensionPoint>
{
	protected ExtensionPointNode(Project project, ExtensionPoint point, ViewSettings viewSettings)
	{
		super(project, point, viewSettings);
	}

	@Nullable
	@Override
	protected PsiElement extractPsiFromValue()
	{
		ExtensionPoint point = getValue();
		return point == null ? null : point.getXmlElement();
	}

	@Nullable
	@Override
	protected Collection<AbstractTreeNode> getChildrenImpl()
	{
		return null;
	}

	@Override
	protected void updateImpl(PresentationData presentationData)
	{
		String name = getValue().getName().getStringValue();
		if(name != null)
			presentationData.setPresentableText(name);
		else
		{
			presentationData.setPresentableText("'name' is required");
			presentationData.setAttributesKey(CodeInsightColors.WRONG_REFERENCES_ATTRIBUTES);
		}
	}
}
