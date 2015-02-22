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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.napile.idea.moduling.dom.Extension;
import org.napile.idea.moduling.dom.ExtensionPoint;
import org.napile.idea.moduling.dom.ExtensionPoints;
import org.napile.idea.moduling.dom.Extensions;
import org.napile.idea.moduling.dom.Module;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.AbstractPsiBasedNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomUtil;
import com.intellij.util.xml.GenericDomValue;

/**
 * @author VISTALL
 * @since 15:34/21.08.12
 */
public class ModuleFileNode extends AbstractPsiBasedNode<XmlFile>
{
	private final Module module;

	public ModuleFileNode(Project project, XmlFile xmlFile, ViewSettings viewSettings, Module module)
	{
		super(project, xmlFile, viewSettings);
		this.module = module;
	}

	@Nullable
	@Override
	protected PsiElement extractPsiFromValue()
	{
		return getValue();
	}

	@Nullable
	@Override
	protected Collection<AbstractTreeNode> getChildrenImpl()
	{
		if(!getSettings().isShowMembers())
			return Collections.emptyList();

		List<AbstractTreeNode> result = new ArrayList<AbstractTreeNode>();

		//add(result, xmlModule.getId());
		//add(result, xmlModule.getName());
		//add(result, xmlModule.getBuildBy());
		//add(result, xmlModule.getVersion());

		List<ExtensionPoint> pointsToShow = new ArrayList<ExtensionPoint>();
		for(ExtensionPoints extensionPoints : module.getExtensionPoints())
			pointsToShow.addAll(extensionPoints.getExtensionPoints());

		if(pointsToShow.size() > 0)
		{
			List<AbstractTreeNode> children = new ArrayList<AbstractTreeNode>(pointsToShow.size());
			for(ExtensionPoint point : pointsToShow)
				children.add(new ExtensionPointNode(getProject(), point, getSettings()));
			result.add(new GroupTreeNode(getProject(), "extension-points", children));
		}

		Map<String, List<Extension>> extensionsToShow = new LinkedHashMap<String, List<Extension>>();

		for(Extensions extensions : module.getExtensions())
		{
			String forId = extensions.getForId().getStringValue();
			if(forId == null)
				continue;

			for(Extension extension : DomUtil.getChildrenOfType(extensions, Extension.class))
			{
				List<Extension> list = extensionsToShow.get(forId);
				if(list == null)
					extensionsToShow.put(forId, list = new ArrayList<Extension>());

				list.add(extension);
			}
		}

		for(Map.Entry<String, List<Extension>> entry : extensionsToShow.entrySet())
		{
			List<Extension> list = entry.getValue();

			if(list.isEmpty())
				continue;

			List<AbstractTreeNode> children = new ArrayList<AbstractTreeNode>(list.size());
			for(Extension extension : list)
				children.add(new ExtensionNode(getProject(), extension, getSettings()));

			result.add(new GroupTreeNode(getProject(), "extensions: " + entry.getKey(), children));
		}

		return result;
	}

	private void add(List<AbstractTreeNode> nodes, GenericDomValue<String> value)
	{
		String string = value.getStringValue();
		if(string != null)
			nodes.add(new SimpleStringTreeNode(getProject(), value.getXmlElementName().toLowerCase() + ": " + string));
	}

	@Override
	protected void updateImpl(PresentationData presentationData)
	{
		GenericDomValue<String> idNode = module.getId();

		String name = idNode.getStringValue();
		if(name != null)
			presentationData.setPresentableText(name);
		else
		{
			presentationData.setPresentableText("'id' is required");
			presentationData.setAttributesKey(CodeInsightColors.WRONG_REFERENCES_ATTRIBUTES);
		}
	}
}
