package me.infamous.kanekaido.common.abilities;

import me.infamous.kanekaido.common.entities.Kaido;

import java.util.function.Consumer;

public enum KaidoAttack {
    ATTACK_A(kaido -> {
        if(!kaido.isAttacking()) kaido.setAttackATimer(Kaido.ATTACK_A_LENGTH);
    }),
    ATTACK_B(kaido -> {
        if(!kaido.isAttacking()) kaido.setAttackBTimer(Kaido.ATTACK_B_LENGTH);
    });

    private final Consumer<Kaido> kaidoAttack;

    KaidoAttack(Consumer<Kaido> kaidoAttack){

        this.kaidoAttack = kaidoAttack;
    }

    public Consumer<Kaido> getKaidoAttack() {
        return this.kaidoAttack;
    }
}
