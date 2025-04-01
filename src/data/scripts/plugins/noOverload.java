package data.scripts.plugins;

import java.util.*;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventAPI;

public class noOverload extends BaseEveryFrameCombatPlugin {

    public noOverload () {}

    CombatEngineAPI engine;
    boolean init = false;

    @Override
    public void init(CombatEngineAPI engine) {
        this.engine = engine;
        this.init = true;
    }

    @Override
    public void advance(float amount, List <InputEventAPI> events) {
        if (!this.init)
            return;
        boolean flag;
        List<ShipAPI> ships = this.engine.getShips();
        for (ShipAPI ship: ships) {
            if (ship.getShield() == null || ship.getFluxLevel() < 0.6)
                continue;
            flag = false;
            if (ship.getFluxLevel() > 0.9 && ship.getHardFluxLevel() > 0.6)
                flag = true;
            if (ship.getHardFluxLevel() > 0.8)
                flag = true;
            if (ship.getHullLevel() < 0.3)
                flag = false;
            if (ship.getAIFlags().hasFlag(ShipwideAIFlags.AIFlags.IN_CRITICAL_DPS_DANGER))
                flag = false;
            if (flag) {
                if (ship.getShield().isOn())
                    ship.getShield().toggleOff();
                else
                    ship.blockCommandForOneFrame(ShipCommand.TOGGLE_SHIELD_OR_PHASE_CLOAK);
            }
        }
    }
}