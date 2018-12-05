package ru.malakhov.nytimes.ui.fragments.toolbar;

public interface MessageToolbarListener {
    void onNextItemSpinner(String selectedItemPosition);
    void onItemToolbarClicked(String keyMenu, String message);
}
