package com.onecode.ffhx.gfx.lib.modal;

public class Sensi {
    public String player_name;
    public int general, red_dot, x2_scope, x4_scope, AWM_scope;

    public Sensi(String player_name, int general, int red_dot, int x2_scope, int x4_scope, int AWM_scope) {
        this.player_name = player_name;
        this.general = general;
        this.red_dot = red_dot;
        this.x2_scope = x2_scope;
        this.x4_scope = x4_scope;
        this.AWM_scope = AWM_scope;
    }
}
