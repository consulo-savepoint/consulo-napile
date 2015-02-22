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

import com.intellij.icons.AllIcons;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author VISTALL
 * @since 15:54/21.08.12
 */
public class GroupTreeNode extends AbstractTreeNode<String>
{
	private final Collection<AbstractTreeNode> children;

	public GroupTreeNode(Project project, String value, Collection<AbstractTreeNode> children)
	{
		super(project, value);
		this.children = children;
	}

	@NotNull
	@Override
	public Collection<? extends AbstractTreeNode> getChildren()
	{
		return children;
	}

	@Override
	protected void update(PresentationData presentationData)
	{
		presentationData.setIcon(AllIcons.Nodes.Package);
		presentationData.setPresentableText(getValue());
	}
}
