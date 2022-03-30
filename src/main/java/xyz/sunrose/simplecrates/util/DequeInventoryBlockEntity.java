package xyz.sunrose.simplecrates.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayDeque;

abstract public class DequeInventoryBlockEntity extends BlockEntity implements Inventory {
    public ArrayDeque<ItemStack> items;

    public DequeInventoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract boolean canPush(ItemStack stack);
    public abstract void push(ItemStack stack);
    public abstract ItemStack pop();
    public abstract ItemStack pop(int count);
    public abstract ItemStack peek();

    public boolean tryPush(ItemStack stack){
        if (canPush(stack)){
            push(stack);
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items==null || items.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        if(slot==0){
            ItemStack top = peek();
            if (top.getCount() < top.getMaxCount()) {
                return top;
            }
            else if (top != ItemStack.EMPTY && canPush(new ItemStack(top.getItem(), 1))) { //if there's room for one more item...
                return new ItemStack(top.getItem(), 0); //make the container look less full so the hopper can toss another item in.
                // TODO test if this fix actually works lol
                // (it doesn't)
            }
            return top;
        }
        return null;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return pop(amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return pop();
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if(slot==0 && canPush(stack)){
            push(stack);
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }



}
