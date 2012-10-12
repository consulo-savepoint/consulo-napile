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

/*
 * @author max
 */
package org.napile.idea.plugin.highlighter;

import org.napile.compiler.lang.parsing.injection.CodeInjectionManager;
import org.napile.compiler.lang.psi.NapileFunctionLiteral;
import org.napile.compiler.lang.psi.NapileFunctionLiteralExpression;
import org.napile.compiler.lexer.NapileTokens;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;

class SoftKeywordsHighlightingVisitor extends HighlightingVisitor
{
	SoftKeywordsHighlightingVisitor(AnnotationHolder holder)
	{
		super(holder);
	}

	@Override
	public void visitElement(PsiElement element)
	{
		if(element instanceof LeafPsiElement)
		{
			IElementType elementType = ((LeafPsiElement) element).getElementType();
			if(NapileTokens.SOFT_KEYWORDS.contains(elementType) || CodeInjectionManager.INSTANCE.getInjectionTokens().contains(elementType))
			{
				TextAttributesKey attributes = JetHighlightingColors.KEYWORD;
				if(NapileTokens.MODIFIER_KEYWORDS.contains(elementType))
				{
					attributes = JetHighlightingColors.BUILTIN_ANNOTATION;
				}
				holder.createInfoAnnotation(element, null).setTextAttributes(attributes);
			}
			if(NapileTokens.SAFE_ACCESS.equals(elementType))
			{
				holder.createInfoAnnotation(element, null).setTextAttributes(JetHighlightingColors.SAFE_ACCESS);
			}
		}
	}

	@Override
	public void visitFunctionLiteralExpression(NapileFunctionLiteralExpression expression)
	{
		if(ApplicationManager.getApplication().isUnitTestMode())
			return;
		NapileFunctionLiteral functionLiteral = expression.getFunctionLiteral();
		holder.createInfoAnnotation(functionLiteral.getOpenBraceNode(), null).setTextAttributes(JetHighlightingColors.FUNCTION_LITERAL_BRACES_AND_ARROW);
		ASTNode closingBraceNode = functionLiteral.getClosingBraceNode();
		if(closingBraceNode != null)
		{
			holder.createInfoAnnotation(closingBraceNode, null).setTextAttributes(JetHighlightingColors.FUNCTION_LITERAL_BRACES_AND_ARROW);
		}
		ASTNode arrowNode = functionLiteral.getArrowNode();
		if(arrowNode != null)
		{
			holder.createInfoAnnotation(arrowNode, null).setTextAttributes(JetHighlightingColors.FUNCTION_LITERAL_BRACES_AND_ARROW);
		}
	}
}
