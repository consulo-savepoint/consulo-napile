/*
 * Copyright 2010-2013 napile.org
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

package org.napile.idea.moduling.resolve;

import java.util.Collection;

import org.napile.compiler.lang.psi.NapileClass;
import org.napile.compiler.lang.psi.NapileClassLike;
import org.napile.idea.plugin.stubindex.NapileFullClassNameIndex;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * @author VISTALL
 * @since 10:49/26.01.13
 */
public class NapileResolver
{
	public static NapileClassLike findNapileClass(String fqName, Project project, GlobalSearchScope searchScope)
	{
		Collection<NapileClass> collection = NapileFullClassNameIndex.getInstance().get(fqName, project, searchScope);
		return collection.size() == 1 ? collection.iterator().next() : null;
	}
}
