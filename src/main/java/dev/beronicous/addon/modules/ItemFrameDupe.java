package dev.beronicous.addon.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ItemFrameDupe extends Module {

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Item> dupeItem = sgGeneral.add(new ItemSetting.Builder()
        .name("Dupe Item")
        .defaultValue(Items.SHULKER_BOX)
        .build());

    private int timer;

    private List<ItemFrameEntity> itemFrames;

    private Vec3d prevPlayerPos;

    private final Setting<Integer> r_delay = sgGeneral.add(new IntSetting.Builder()
        .name("Right click delay")
        .range(1,20)
        .defaultValue(5)
        .build());

    private final Setting<Integer> l_delay = sgGeneral.add(new IntSetting.Builder()
        .name("Left click delay")
        .range(1,20)
        .defaultValue(1)
        .build());

    private final Setting<Integer> sgRange = sgGeneral.add(new IntSetting.Builder()
        .name("Range")
        .range(1,4)
        .defaultValue(3)
        .build());
    public ItemFrameDupe(){
        super(Categories.World,"item-frame-dupe","Item frame dupe");
    }

    @Override
    public void onActivate(){
        timer=0;
        prevPlayerPos=mc.player.getPos();
        if(!(InvUtils.find(dupeItem.get()).found())){
            error("Dupe item not found. Disabling...");
            toggle();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTick(TickEvent.Pre event){

        if(prevPlayerPos!=mc.player.getPos()){
            error("Player moved. Disabling item frame duper...");
            toggle();
            return;
        }

        double range=sgRange.get();
        itemFrames = mc.world.getEntitiesByClass(ItemFrameEntity.class, new Box(mc.player.getPos().add(-range,-range,-range),mc.player.getPos().add(range,range,range)), itemFrameEntity -> true);

        if(itemFrames.isEmpty()){
            error("No item frames found. Disabling item frame duper...");
            toggle();
            return;
        }

        FindItemResult item=InvUtils.find(dupeItem.get());
        InvUtils.move().from(item.slot()).to(0);

        InvUtils.swap(0,false);


        for(ItemFrameEntity itemFrame: itemFrames){
            if(timer%(20/r_delay.get())==0) mc.interactionManager.interactEntity(mc.player,itemFrame, Hand.MAIN_HAND);

            if(timer%(20/l_delay.get())==0 && itemFrame.getHeldItemStack().getCount()>0) mc.interactionManager.attackEntity(mc.player, itemFrame);
        }
        timer++;
    }





}
