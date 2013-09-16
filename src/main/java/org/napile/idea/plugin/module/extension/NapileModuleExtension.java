package org.napile.idea.plugin.module.extension;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.SdkType;
import org.consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import org.jetbrains.annotations.NotNull;
import org.napile.idea.plugin.sdk.NapileSdkType;

/**
 * @author VISTALL
 * @since 12:24/27.05.13
 */
public class NapileModuleExtension extends ModuleExtensionWithSdkImpl<NapileModuleExtension>
{
	public NapileModuleExtension(@NotNull String id, @NotNull Module module)
	{
		super(id, module);
	}

	@Override
	protected Class<? extends SdkType> getSdkTypeClass()
	{
		return NapileSdkType.class;
	}
}
