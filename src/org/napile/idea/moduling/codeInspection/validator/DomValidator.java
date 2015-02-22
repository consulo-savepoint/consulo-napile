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

package org.napile.idea.moduling.codeInspection.validator;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.napile.idea.moduling.dom.Module;
import com.intellij.util.xml.highlighting.BasicDomElementsInspection;

/**
 * @author VISTALL
 * @since 17:18/21.08.12
 */
public class DomValidator extends BasicDomElementsInspection<Module>
{
	public DomValidator()
	{
		super(Module.class);
	}

	@NotNull
	public String getShortName()
	{
		return getClass().getSimpleName();
	}

	@Nls
	@NotNull
	public String getGroupDisplayName()
	{
		return "Napile";
	}

}
