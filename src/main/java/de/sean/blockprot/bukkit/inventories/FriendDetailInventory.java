package de.sean.blockprot.bukkit.inventories;

import de.sean.blockprot.TranslationKey;
import de.sean.blockprot.Translator;
import de.sean.blockprot.bukkit.nbt.FriendModifyAction;
import de.sean.blockprot.bukkit.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The detail inventory for managing a single friend and their permission.
 */
public final class FriendDetailInventory extends FriendModifyInventory {
    @Override
    public int getSize() {
        return InventoryConstants.singleLine;
    }

    @NotNull
    @Override
    public String getTranslatedInventoryName() {
        return Translator.get(TranslationKey.INVENTORIES__FRIENDS__EDIT);
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event, @NotNull InventoryState state) {
        final Player player = (Player)event.getWhoClicked();
        final ItemStack item = event.getCurrentItem();
        if (item == null) return;

        switch (item.getType()) {
            case BLACK_STAINED_GLASS_PANE: {
                player.openInventory(new FriendManageInventory().fill(player));
                break;
            }
            case RED_STAINED_GLASS_PANE: {
                OfflinePlayer friend = state.getCurFriend();
                assert friend != null;
                modifyFriendsForAction(state, player, friend, FriendModifyAction.REMOVE_FRIEND, false);
                player.openInventory(new FriendManageInventory().fill(player));
                break;
            }
            case PLAYER_HEAD: {
                break; // Don't do anything.
            }
            default: exit(player);
        }
        event.setCancelled(true);
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event, @NotNull InventoryState state) {}

    public Inventory fill(@NotNull Player player) {
        final InventoryState state = InventoryState.Companion.get(player.getUniqueId());
        if (state == null) return inventory;

        inventory.setItem(
            0,
            ItemUtil.INSTANCE.getPlayerSkull(Objects.requireNonNull(state.getCurFriend()))
        );
        setItemStack(
            1,
            Material.RED_STAINED_GLASS_PANE,
            TranslationKey.INVENTORIES__FRIENDS__REMOVE
        );
        setBackButton();

        return inventory;
    }
}