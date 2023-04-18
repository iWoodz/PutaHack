package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;

public
class ItemPhysics
        extends Module {
    public static ItemPhysics INSTANCE = new ItemPhysics ( );
    public final Setting< Float > Scaling = this.register ( new Setting <> ( "Scaling" , 0.5f , 0.0f , 10.0f ) );

    public
    ItemPhysics ( ) {
        super ( "ItemPhysics" , "Apply physics to items." , Category.RENDER , false , false , false );
        this.setInstance ( );
    }

    public static
    ItemPhysics getInstance ( ) {
        if ( INSTANCE == null ) {
            INSTANCE = new ItemPhysics ( );
        }
        return INSTANCE;
    }

    private
    void setInstance ( ) {
        INSTANCE = this;
    }
}
