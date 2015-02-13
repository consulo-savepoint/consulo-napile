package org.napile.idea.plugin.module.extension;

import org.consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import org.jetbrains.annotations.NotNull;
import org.napile.idea.plugin.sdk.NapileSdkType;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 12:24/27.05.13
 */
public class NapileModuleExtension extends ModuleExtensionWithSdkImpl<NapileModuleExtension>
{
	public NapileModuleExtension(@NotNull String id, @NotNull ModuleRootLayer module)
	{
		super(id, module);
	}

	@NotNull
	@Override
	public Class<? extends SdkType> getSdkTypeClass()
	{
		return NapileSdkType.class;
	}
}
