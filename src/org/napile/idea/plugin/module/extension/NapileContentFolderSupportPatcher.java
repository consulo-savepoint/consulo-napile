package org.napile.idea.plugin.module.extension;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.roots.ContentFolderSupportPatcher;
import org.mustbe.consulo.roots.ContentFolderTypeProvider;
import org.mustbe.consulo.roots.impl.ProductionContentFolderTypeProvider;
import org.mustbe.consulo.roots.impl.ProductionResourceContentFolderTypeProvider;
import org.mustbe.consulo.roots.impl.TestContentFolderTypeProvider;
import org.mustbe.consulo.roots.impl.TestResourceContentFolderTypeProvider;
import com.intellij.openapi.roots.ModifiableRootModel;

/**
 * @author VISTALL
 * @since 13.02.15
 */
public class NapileContentFolderSupportPatcher implements ContentFolderSupportPatcher
{
	@Override
	public void patch(@NotNull ModifiableRootModel model, @NotNull Set<ContentFolderTypeProvider> set)
	{
		NapileModuleExtension extension = model.getExtension(NapileModuleExtension.class);
		if(extension != null)
		{
			set.add(ProductionContentFolderTypeProvider.getInstance());
			set.add(ProductionResourceContentFolderTypeProvider.getInstance());
			set.add(ProductionResourceContentFolderTypeProvider.getInstance());
			set.add(TestContentFolderTypeProvider.getInstance());
			set.add(TestResourceContentFolderTypeProvider.getInstance());
		}
	}
}
