package tachiyomi.ui.browse.components

import androidx.compose.runtime.Composable
import tachiyomi.domain.catalog.model.Catalog

@Composable
internal actual fun CatalogMenuButton(
  catalog: Catalog,
  onPinToggle: (() -> Unit)?,
  onUninstall: (() -> Unit)?
) {
  // TODO not implemented in desktop (waiting for multiplatform DropdownMenu so we don't need this)
}
