package org.napile.idea.plugin.psi.impl;

import com.intellij.lang.Language;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.file.PsiPackageBase;
import com.intellij.util.ArrayFactory;
import org.consulo.module.extension.ModuleExtension;
import org.consulo.psi.PsiPackage;
import org.consulo.psi.PsiPackageManager;
import org.jetbrains.annotations.NotNull;
import org.napile.compiler.lang.NapileLanguage;

/**
 * @author VISTALL
 * @since 12:36/27.05.13
 */
public class NapilePackage extends PsiPackageBase
{
	public static final NapilePackage[] EMPTY_ARRAY = new NapilePackage[0];
	public static final ArrayFactory<NapilePackage> ARRAY_FACTORY = new ArrayFactory<NapilePackage>()
	{
		@NotNull
		@Override
		public NapilePackage[] create(int i)
		{
			return i == 0 ? EMPTY_ARRAY : new NapilePackage[i];
		}
	};

	public NapilePackage(PsiManager manager, PsiPackageManager packageManager, Class<? extends ModuleExtension> extensionClass, String qualifiedName)
	{
		super(manager, packageManager, extensionClass, qualifiedName);
	}

	@Override
	protected ArrayFactory<? extends PsiPackage> getPackageArrayFactory()
	{
		return ARRAY_FACTORY;
	}

	@NotNull
	@Override
	public Language getLanguage()
	{
		return NapileLanguage.INSTANCE;
	}
}
