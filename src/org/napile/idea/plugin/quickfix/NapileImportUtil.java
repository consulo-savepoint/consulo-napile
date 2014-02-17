package org.napile.idea.plugin.quickfix;

import org.jetbrains.annotations.Nullable;
import org.napile.asm.resolve.ImportPath;
import org.napile.asm.resolve.name.FqNameUnsafe;
import org.napile.compiler.lang.psi.IfNotParsed;
import org.napile.compiler.lang.psi.NapileExpression;
import org.napile.compiler.lang.psi.NapileImportDirective;

/**
 * @author VISTALL
 * @since 13:37/24.04.13
 */
public class NapileImportUtil
{
	@Nullable
	@IfNotParsed
	public static ImportPath getImportPath(NapileImportDirective importDirective)
	{
		final NapileExpression importedReference = importDirective.getImportedReference();
		if(importedReference == null)
			return null;

		final String text = importedReference.getText();
		final String importPath = text.replaceAll(" ", "") + (importDirective.isAllUnder() ? ".*" : "");
		if(!FqNameUnsafe.isValid(importPath))
			return null;

		return new ImportPath(importPath);
	}
}
