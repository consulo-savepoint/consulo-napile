/*
 * Copyright 2010-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.jet.plugin;

import javax.swing.Icon;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.PlatformIcons;

/**
 * @author Nikolay Krasko
 */
public interface JetIcons
{
	Icon SMALL_LOGO = IconLoader.getIcon("/org/jetbrains/jet/plugin/icons/kotlin.png");

	Icon CLASS = IconLoader.getIcon("/org/napile/icons/nodes/class.png");
	Icon ENUM = IconLoader.getIcon("/org/napile/icons/nodes/enum.png");
	Icon ABSTRACT_CLASS = IconLoader.getIcon("/org/napile/icons/nodes/abstractClass.png");

	Icon C_HERITABLE = IconLoader.getIcon("/org/napile/icons/gutter/c_heritable.png");

	Icon FILE = IconLoader.getIcon("/org/napile/icons/fileTypes/napile.png");
	Icon OBJECT = IconLoader.getIcon("/org/jetbrains/jet/plugin/icons/object.png");

	Icon FUNCTION = IconLoader.getIcon("/org/jetbrains/jet/plugin/icons/function.png");
	Icon EXTENSION_FUNCTION = PlatformIcons.FUNCTION_ICON;
	Icon LAMBDA = IconLoader.getIcon("/org/jetbrains/jet/plugin/icons/lambda.png");
	Icon VAR = IconLoader.getIcon("/org/jetbrains/jet/plugin/icons/variable.png");
	Icon VAL = IconLoader.getIcon("/org/jetbrains/jet/plugin/icons/value.png");
	Icon PARAMETER = IconLoader.getIcon("/org/jetbrains/jet/plugin/icons/parameter.png");
	Icon FIELD_VAL = IconLoader.getIcon("/org/jetbrains/jet/plugin/icons/field_value.png");
	Icon FIELD_VAR = IconLoader.getIcon("/org/jetbrains/jet/plugin/icons/field_variable.png");

	Icon LAUNCH = IconLoader.getIcon("/org/jetbrains/jet/plugin/icons/kotlin_launch_configuration.png");
}
