package org.napile.idea.plugin.module.extension;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModifiableRootModel;
import org.consulo.module.extension.MutableModuleExtensionWithSdk;
import org.consulo.module.extension.MutableModuleInheritableNamedPointer;
import org.consulo.module.extension.ui.ModuleExtensionWithSdkPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author VISTALL
 * @since 12:24/27.05.13
 */
public class NapileMutableModuleExtension extends NapileModuleExtension implements MutableModuleExtensionWithSdk<NapileModuleExtension>
{
	@NotNull
	private final NapileModuleExtension moduleExtension;

	public NapileMutableModuleExtension(@NotNull String id, @NotNull Module module, @NotNull NapileModuleExtension moduleExtension)
	{
		super(id, module);
		this.moduleExtension = moduleExtension;

		commit(moduleExtension);
	}

	@Nullable
	@Override
	public JComponent createConfigurablePanel(@NotNull ModifiableRootModel modifiableRootModel, @Nullable Runnable runnable)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new ModuleExtensionWithSdkPanel(this, runnable), BorderLayout.NORTH);
		return panel;
	}

	@NotNull
	@Override
	public MutableModuleInheritableNamedPointer<Sdk> getInheritableSdk()
	{
		return (MutableModuleInheritableNamedPointer<Sdk>) super.getInheritableSdk();
	}

	@Override
	public void setEnabled(boolean b)
	{
		myIsEnabled = b;
	}

	@Override
	public boolean isModified()
	{
		return isModifiedImpl(moduleExtension);
	}

	@Override
	public void commit()
	{
		moduleExtension.commit(this);
	}
}
