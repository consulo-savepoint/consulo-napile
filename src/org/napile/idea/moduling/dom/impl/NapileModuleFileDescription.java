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

package org.napile.idea.moduling.dom.impl;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.idea.moduling.dom.Module;
import org.napile.idea.moduling.util.NapileModulingConstants;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;

/**
 * @author VISTALL
 * @since 0:41/18.08.12
 */
public class NapileModuleFileDescription extends DomFileDescription<Module>
{
	public NapileModuleFileDescription()
	{
		super(Module.class, "module");
	}

	@Override
	public boolean isMyFile(@NotNull XmlFile file, @Nullable final com.intellij.openapi.module.Module module)
	{
		return file.getName().equals(NapileModulingConstants.MODULE_FILE_NAME);
	}

	public Icon getFileIcon(@Iconable.IconFlags int flags)
	{
		return NapileModulingConstants.FILE_TYPE;
	}
}
