package org.napile.idea.plugin.sdk;

import com.intellij.openapi.projectRoots.*;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.idea.plugin.NapileIcons;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 16.09.13.
 */
public class NapileSdkType extends SdkType {
	public NapileSdkType() {
		super("NAPILE_SDK");
	}

	@Nullable
	@Override
	public Icon getIcon()
	{
		return NapileIcons.LOGO_16_16;
	}

	@Nullable
	@Override
	public Icon getGroupIcon()
	{
		return getIcon();
	}

	@Nullable
	@Override
	public String suggestHomePath() {
		return null;
	}

	@Override
	public boolean isValidSdkHome(String s) {
		return true;
	}

	@Nullable
	@Override
	public String getVersionString(String s) {
		return "1.0";
	}

	@Override
	public String suggestSdkName(String s, String s2) {
		return s;
	}

	@Nullable
	@Override
	public AdditionalDataConfigurable createAdditionalDataConfigurable(SdkModel sdkModel, SdkModificator sdkModificator) {
		return null;
	}

	@NotNull
	@Override
	public String getPresentableName() {
		return "Napile SDK";
	}

	@Override
	public void saveAdditionalData(SdkAdditionalData sdkAdditionalData, Element element) {

	}
}
