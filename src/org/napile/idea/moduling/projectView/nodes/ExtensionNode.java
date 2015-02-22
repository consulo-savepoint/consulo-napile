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
import org.napile.idea.moduling.dom.Extension;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.AbstractPsiBasedNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 16:07/21.08.12
 */
public class ExtensionNode extends AbstractPsiBasedNode<Extension>
{
	public ExtensionNode(Project project, Extension extension, ViewSettings viewSettings)
	{
		super(project, extension, viewSettings);
	}

	@Nullable
	@Override
	protected PsiElement extractPsiFromValue()
	{
		Extension extension = getValue();
		return extension == null ? null : extension.getXmlElement();
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
		presentationData.setPresentableText(getValue().getXmlElementName());
	}
}
