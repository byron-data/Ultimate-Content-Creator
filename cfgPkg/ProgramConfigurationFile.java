package cfgPkg;

import java.io.Serializable;

public class ProgramConfigurationFile implements Serializable {
    public boolean expertMode   = true;
    public String  editor       = cfgPkg.Const.defaultLoc;
    public boolean nativeEdit   = false;
    public boolean nativeBrowse = false;
}
