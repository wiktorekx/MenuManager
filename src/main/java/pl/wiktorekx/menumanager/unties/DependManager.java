package pl.wiktorekx.menumanager.unties;

import org.bukkit.Bukkit;

public class DependManager {

    public static <T> T dependPlugin(String name, PluginDependCallback<T> pluginDependCallback) {
        if(Bukkit.getPluginManager().isPluginEnabled(name)) {
            return pluginDependCallback.onResultSuccess();
        } else {
            pluginDependCallback.onResultDeny();
        }
        return null;
    }

    public interface PluginDependCallback<T> {

        T onResultSuccess();

        void onResultDeny();
    }

    public static abstract class ForcePluginDependCallback<T> implements PluginDependCallback<T> {

        @Override
        public void onResultDeny() {
            try {
                throw new PluginDependException();
            } catch (PluginDependException e) {
                e.printStackTrace();
            }
        }
    }

    public static abstract class SimplePluginDependCallback<T> implements PluginDependCallback<T> {

        @Override
        public void onResultDeny() {}
    }

    public static class TruePluginDependCallback extends SimplePluginDependCallback<Boolean> {
        @Override
        public Boolean onResultSuccess() {
            return true;
        }
    }


    public static class PluginDependException extends Exception {}
}
