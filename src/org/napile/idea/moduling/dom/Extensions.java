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

package org.napile.idea.moduling.dom;

import org.jetbrains.annotations.NotNull;
import org.napile.idea.moduling.dom.converters.ModuleConverter;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;

/**
 * @author VISTALL
 * @since 1:14/18.08.12
 */
public interface Extensions extends DomElement
{
	@Required
	@NotNull
	@Attribute("for-id")
	@Convert(value = ModuleConverter.class)
	GenericAttributeValue<String> getForId();
}
