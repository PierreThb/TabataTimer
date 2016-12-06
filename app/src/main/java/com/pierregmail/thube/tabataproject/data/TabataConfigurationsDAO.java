package com.pierregmail.thube.tabataproject.data;

import java.util.List;

/**
 * Created by Pierre on 04/11/2016.
 */

public class TabataConfigurationsDAO {
    public static List<TabataConfigurations> selectAll(){
        return TabataConfigurations.listAll(TabataConfigurations.class);
    }
}
