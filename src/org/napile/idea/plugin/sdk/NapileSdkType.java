package org.napile.idea.plugin.sdk;

import java.io.File;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.idea.plugin.NapileIcons;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.util.ArchiveVfsUtil;

/**
 * @author VISTALL
 * @since 16.09.13.
 */
public class NapileSdkType extends SdkType
{
	@NotNull
	public static NapileSdkType getInstance()
	{
		return EP_NAME.findExtension(NapileSdkType.class);
	}

	public NapileSdkType()
	{
		super("NAPILE_SDK");
	}

	@Override
	public void setupSdkPaths(Sdk sdk)
	{
		SdkModificator sdkModificator = sdk.getSdkModificator();

		File file = new File(sdk.getHomePath(), "lib");

		VirtualFile fileByIoFile = LocalFileSystem.getInstance().findFileByIoFile(file);
		if(fileByIoFile != null)
		{
			sdkModificator.addRoot(fileByIoFile, OrderRootType.SOURCES);
		}

		VirtualFile rtNzip = LocalFileSystem.getInstance().findFileByIoFile(new File(sdk.getHomePath(), "rt.nzip"));
		if(rtNzip != null)
		{
			VirtualFile jarRootForLocalFile = ArchiveVfsUtil.getJarRootForLocalFile(rtNzip);
			if(jarRootForLocalFile != null)
			{
				sdkModificator.addRoot(jarRootForLocalFile, OrderRootType.CLASSES);
			}
		}
		sdkModificator.commitChanges();
	}

	@Override
	public boolean isRootTypeApplicable(OrderRootType type)
	{
		return type == OrderRootType.SOURCES || type == OrderRootType.CLASSES;
	}

	@Nullable
	@Override
	public Icon getIcon()
	{
		return NapileIcons.LOGO_16_16;
	}

	@Override
	public boolean isValidSdkHome(String s)
	{
		return new File(s, "lib").exists();
	}

	@Nullable
	@Override
	public String getVersionString(String s)
	{
		return "1.0";
	}

	@Override
	public String suggestSdkName(String s, String sdkHome)
	{
		return new File(sdkHome).getName();
	}


	@NotNull
	@Override
	public String getPresentableName()
	{
		return "Napile SDK";
	}
}
