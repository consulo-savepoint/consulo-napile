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

package org.napile.idea.moduling.util;

import javax.swing.Icon;

import com.intellij.openapi.util.IconLoader;

/**
 * @author VISTALL
 * @since 0:42/18.08.12
 */
public interface NapileModulingConstants
{
	Icon FILE_TYPE = IconLoader.findIcon("/org/napile/idea/moduling/icons/moduleFile.png");

	String MODULE_FILE_TEMPLATE_NAME = "@module@";

	String MODULE_FILE_NAME = "@module@.xml";
}
