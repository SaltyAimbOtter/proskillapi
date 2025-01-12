/**
 * SkillAPI
 * com.sucy.skill.dynamic.mechanic.FireMechanic
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Steven Sucy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.sucy.skill.dynamic.mechanic;

import com.sucy.skill.SkillAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * Executes child components after a delay
 */
public class FireMechanic extends MechanicComponent {
    private static final String SECONDS = "seconds";
    private static final String DAMAGE = "damage";
    public static final String META_KEY = "fireMechanic";

    @Override
    public String getKey() {
        return "fire";
    }

    /**
     * Executes the component
     *
     * @param caster  caster of the skill
     * @param level   level of the skill
     * @param targets targets to apply to
     *
     * @param force
     * @return true if applied to something, false otherwise
     */
    @Override
    public boolean execute(LivingEntity caster, int level, List<LivingEntity> targets, boolean force) {
        if (targets.size() == 0) {
            return false;
        }
        double seconds = parseValues(caster, SECONDS, level, 3.0);
        double damage = parseValues(caster, DAMAGE, level, 1);
        int ticks = (int) (seconds * 20);
        for (LivingEntity target : targets) {
            int newTicks = ticks <= 0 ? 0 : Math.max(ticks, target.getFireTicks());
            target.setFireTicks(newTicks);
            SkillAPI.setMeta(target, META_KEY, damage);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (target.hasMetadata(META_KEY)) {
                        SkillAPI.removeMeta(target, META_KEY);
                    }
                }
            }.runTaskLater(SkillAPI.inst(), newTicks);
        }
        return targets.size() > 0;
    }
}
