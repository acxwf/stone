package com.github.acxwf.stone

import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class StoneListenerEvent : org.bukkit.event.Listener {

    @EventHandler
    fun doLaunchStone(e: PlayerInteractEvent) {
        val pl: Player = e.player
        val stone: Material = Material.COBBLESTONE

        if (pl.getCooldown(stone) != 0) return
        if(e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) return

        if (pl.gameMode != GameMode.SPECTATOR || e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_AIR) {
            if (pl.itemInHand.type == Material.COBBLESTONE) {
                val force: Float = 1.25f

                val loc: Location = pl.eyeLocation

                val cobSt = pl.world.spawn(loc, Snowball::class.java)
                cobSt.item = ItemStack(stone)
                cobSt.shooter = pl
                cobSt.velocity = loc.direction.multiply(force)

                pl.world.playSound(loc, Sound.ENTITY_ENDER_PEARL_THROW, .3f, 1f)
                pl.setCooldown(stone, 3)

                if (pl.gameMode != GameMode.CREATIVE || pl.gameMode != GameMode.SPECTATOR) {
                    pl.itemInHand.amount--
                    pl.setItemInHand(pl.itemInHand)

                    if (pl.itemInHand.amount == 0) {
                        pl.sendActionBar("${ChatColor.WHITE}더 이상 던질 돌이 없습니다")
                        pl.world.playSound(loc, Sound.BLOCK_WOODEN_TRAPDOOR_CLOSE, 0.4f, 2f)
                    }

                }
            }
        }
    }

    @EventHandler fun onHitOfCobble(e: ProjectileHitEvent){
        //Hit Player


        if(e.entity.shooter is Player || (e.entity as Snowball).item.type == Material.COBBLESTONE || e.entity is Snowball){
            if(e.hitEntity is LivingEntity){

                val en:LivingEntity = e.hitEntity as LivingEntity
                val cobble:Snowball = e.entity as Snowball


                cobble.world.spawnParticle(Particle.BLOCK_CRACK, cobble.location, 16, .0, .0, .0, .0, Material.COBBLESTONE.createBlockData(), true)
                cobble.world.playSound(cobble.location, Sound.BLOCK_STONE_BREAK, .5f, 1f)
                en.world.playSound(en.location, Sound.ENTITY_PLAYER_HURT, 1f, 1f)

                en.noDamageTicks = 0
                en.damage(1.0, cobble.shooter as Player)


            }else{
                if(e.hitBlock is Block){
                    val cobble:Snowball = e.entity as Snowball
                    val block:Block = e.hitBlock!! //not null

                    cobble.world.spawnParticle(Particle.BLOCK_CRACK, cobble.location, 16, .0, .0, .0, .0, Material.COBBLESTONE.createBlockData(), true)
                    cobble.world.playSound(cobble.location, Sound.BLOCK_STONE_BREAK, .5f, 1f)

                    //random break
                    if(block.type == Material.OBSIDIAN){
                        val rand = (Math.random()*100+1).toInt()
                        if(rand < 3){
                            block.breakNaturally(ItemStack(Material.GOLDEN_PICKAXE))
                        }
                    }else if(block.type == Material.END_STONE){
                        val rand = (Math.random()*100+1).toInt()
                        if(rand < 5){
                            block.breakNaturally(ItemStack(Material.GOLDEN_PICKAXE))
                        }
                    }else if(block.type == Material.BEDROCK){
                        cobble.world.spawnParticle(Particle.BLOCK_CRACK, cobble.location, 4, .0, .0, .0, .0, Material.IRON_BLOCK.createBlockData(), true)
                    }else{
                        val rand = (Math.random()*100+1).toInt()
                        if(rand<10){
                            block.breakNaturally(ItemStack(Material.DIRT))
                        }
                    }

                }else{
                    return
                }
            }
        }
    }
}
