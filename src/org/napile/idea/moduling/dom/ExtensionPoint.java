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

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.compiler.lang.psi.NapileClassLike;
import org.napile.idea.moduling.dom.converters.ToNapileClassConverter;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import com.intellij.util.xml.SubTagList;

/**
 * @author VISTALL
 * @since 1:06/18.08.12
 */
public interface ExtensionPoint extends DomElement
{
	@Required
	@NotNull
	GenericAttributeValue<String> getName();

	@Required
	@Convert(value = ToNapileClassConverter.class)
	@NotNull
	GenericAttributeValue<NapileClassLike> getDescriptor();

	@NotNull
	@SubTagList(value = "ref-attribute-to-type-parameter")
	List<RefAttributeToTypeParameter> getRefAttributeToTypeParameterList();
}
