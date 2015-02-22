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

package org.napile.idea.moduling.projectView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.napile.idea.moduling.dom.Module;
import org.napile.idea.moduling.projectView.nodes.ModuleFileNode;
import com.intellij.ide.projectView.SelectableTreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;

/**
 * @author VISTALL
 * @since 15:32/21.08.12
 */
public class TreeStructureProviderImpl implements SelectableTreeStructureProvider
{
	@Nullable
	@Override
	public PsiElement getTopLevelElement(PsiElement element)
	{
		return null;
	}

	@Override
	public Collection<AbstractTreeNode> modify(AbstractTreeNode abstractTreeNode, Collection<AbstractTreeNode> abstractTreeNodes, ViewSettings viewSettings)
	{
		List<AbstractTreeNode> result = new ArrayList<AbstractTreeNode>(abstractTreeNodes.size());
		for(AbstractTreeNode node : abstractTreeNodes)
		{
			Object element = node.getValue();
			if(element instanceof XmlFile)
			{
				XmlFile xmlFile = (XmlFile) element;
				XmlTag xmlTag = xmlFile.getRootTag();

				DomElement domElement = DomUtil.getDomElement(xmlTag);
				if(domElement instanceof Module)
				{
					result.add(new ModuleFileNode(abstractTreeNode.getProject(), (XmlFile) element, viewSettings, (Module) domElement));
					continue;
				}
			}

			result.add(node);
		}
		return result;
	}

	@Nullable
	@Override
	public Object getData(Collection<AbstractTreeNode> abstractTreeNodes, String s)
	{
		return null;
	}
}
