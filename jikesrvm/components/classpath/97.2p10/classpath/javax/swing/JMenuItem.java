/* JMenuItem.java --
   Copyright (C) 2002, 2004, 2005, 2006  Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package javax.swing;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.plaf.MenuItemUI;

/**
 * JMenuItem represents element in the menu. It inherits most of
 * its functionality from AbstractButton, however its behavior somewhat
 * varies from it. JMenuItem fire different kinds of events.
 * PropertyChangeEvents are fired when menuItems properties are modified;
 * ChangeEvents are fired when menuItem's state changes and actionEvents are
 * fired when menu item is selected. In addition to this events menuItem also
 * fire MenuDragMouseEvent and MenuKeyEvents when mouse is dragged over
 * the menu item or associated key with menu item is invoked respectively.
 */
public class JMenuItem extends AbstractButton implements Accessible,
                                                         MenuElement
{
  private static final long serialVersionUID = -1681004643499461044L;

  /** Combination of keyboard keys that can be used to activate this menu item */
  private KeyStroke accelerator;

  /**
   * Indicates if we are currently dragging the mouse.
   */
  private boolean isDragging;

  /**
   * Creates a new JMenuItem object.
   */
  public JMenuItem()
  {
    this(null, null);
  }

  /**
   * Creates a new JMenuItem with the given icon.
   *
   * @param icon Icon that will be displayed on the menu item
   */
  public JMenuItem(Icon icon)
  {
    // FIXME: The requestedFocusEnabled property should
    // be set to false, when only icon is set for menu item.
    this(null, icon);
  }

  /**
   * Creates a new JMenuItem with the given label.
   *
   * @param text label for the menu item
   */
  public JMenuItem(String text)
  {
    this(text, null);
  }

  /**
   * Creates a new JMenuItem associated with the specified action.
   *
   * @param action action for this menu item
   */
  public JMenuItem(Action action)
  {
    super();
    super.setAction(action);
    setModel(new DefaultButtonModel());
    init(null, null);
    if (action != null)
      {
	String name = (String) action.getValue(Action.NAME);
	if (name != null)
          setName(name);

	KeyStroke accel = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
	if (accel != null)
          setAccelerator(accel);

	Integer mnemonic = (Integer) action.getValue(Action.MNEMONIC_KEY);
	if (mnemonic != null)
          setMnemonic(mnemonic.intValue());

	String command = (String) action.getValue(Action.ACTION_COMMAND_KEY);
	if (command != null)
          setActionCommand(command);
      }
  }

  /**
   * Creates a new JMenuItem with specified text and icon.
   * Text is displayed to the left of icon by default.
   *
   * @param text label for this menu item
   * @param icon icon that will be displayed on this menu item
   */
  public JMenuItem(String text, Icon icon)
  {
    super();
    setModel(new DefaultButtonModel());
    init(text, icon);
  }

  /**
   * Creates a new JMenuItem object.
   *
   * @param text label for this menu item
   * @param mnemonic - Single key that can be used with a
   * look-and-feel meta key to activate this menu item. However
   * menu item should be visible on the screen when mnemonic is used.
   */
  public JMenuItem(String text, int mnemonic)
  {
    this(text, null);
    setMnemonic(mnemonic);
  }

  /**
   * Initializes this menu item
   *
   * @param text label for this menu item
   * @param icon icon to be displayed for this menu item
   */
  protected void init(String text, Icon icon)
  {
    super.init(text, icon);

    // Initializes properties for this menu item, that are different
    // from Abstract button properties. 
    /* NOTE: According to java specifications paint_border should be set to false,
      since menu item should not have a border. However running few java programs
      it seems that menu items and menues can have a border. Commenting
      out statement below for now. */
    //borderPainted = false;
    focusPainted = false;
    horizontalAlignment = JButton.LEADING;
    horizontalTextPosition = JButton.TRAILING;
  }

  /**
   * Set the "UI" property of the menu item, which is a look and feel class
   * responsible for handling menuItem's input events and painting it.
   *
   * @param ui The new "UI" property
   */
  public void setUI(MenuItemUI ui)
  {
    super.setUI(ui);
  }
  
  /**
   * This method sets this menuItem's UI to the UIManager's default for the
   * current look and feel.
   */
  public void updateUI()
  {
    setUI((MenuItemUI) UIManager.getUI(this));
  }

  /**
   * This method returns a name to identify which look and feel class will be
   * the UI delegate for the menuItem.
   *
   * @return The Look and Feel classID. "MenuItemUI"
   */
  public String getUIClassID()
  {
    return "MenuItemUI";
  }

  /**
   * Returns true if button's model is armed and false otherwise. The
   * button model is armed if menu item has focus or it is selected.
   *
   * @return $boolean$ true if button's model is armed and false otherwise
   */
  public boolean isArmed()
  {
    return getModel().isArmed();
  }

  /**
   * Sets menuItem's "ARMED" property
   *
   * @param armed DOCUMENT ME!
   */
  public void setArmed(boolean armed)
  {
    getModel().setArmed(armed);
  }

  /**
   * Enable or disable menu item. When menu item is disabled,
   * its text and icon are grayed out if they exist.
   *
   * @param enabled if true enable menu item, and disable otherwise.
   */
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
  }

  /**
   * Return accelerator for this menu item.
   *
   * @return $KeyStroke$ accelerator for this menu item.
   */
  public KeyStroke getAccelerator()
  {
    return accelerator;
  }

  /**
   * Sets the key combination which invokes the menu item's action 
   * listeners without navigating the menu hierarchy. Note that when the 
   * keyboard accelerator is typed, it will work whether or not the 
   * menu is currently displayed.
   * 
   * @param keystroke accelerator for this menu item.
   */
  public void setAccelerator(KeyStroke keystroke)
  {
    KeyStroke old = this.accelerator;
    this.accelerator = keystroke;
    firePropertyChange ("accelerator", old, keystroke);
  }

  /**
   * Configures menu items' properties from properties of the specified action.
   * This method overrides configurePropertiesFromAction from AbstractButton
   * to also set accelerator property.
   *
   * @param action action to configure properties from
   */
  protected void configurePropertiesFromAction(Action action)
  {
    super.configurePropertiesFromAction(action);

    if (! (this instanceof JMenu) && action != null)
      {
        setAccelerator((KeyStroke) (action.getValue(Action.ACCELERATOR_KEY)));
        if (accelerator != null)
          super.registerKeyboardAction(action, accelerator, 
                                       JComponent.WHEN_IN_FOCUSED_WINDOW);
      }
  }

  /**
   * Creates PropertyChangeListener to listen for the changes in action
   * properties.
   *
   * @param action action to listen to for property changes
   *
   * @return $PropertyChangeListener$ Listener that listens to changes in
   * action properties.
   */
  protected PropertyChangeListener createActionPropertyChangeListener(Action action)
  {
    return new PropertyChangeListener()
      {
	public void propertyChange(PropertyChangeEvent e)
	{
	  Action act = (Action) (e.getSource());
	  configurePropertiesFromAction(act);
	}
      };
  }

  /**
   * Process mouse events forwarded from MenuSelectionManager.
   *
   * @param ev event forwarded from MenuSelectionManager
   * @param path path to the menu element from which event was generated
   * @param manager MenuSelectionManager for the current menu hierarchy
   */
  public void processMouseEvent(MouseEvent ev, MenuElement[] path,
                                MenuSelectionManager manager)
  {
    MenuDragMouseEvent e = new MenuDragMouseEvent(ev.getComponent(),
                                                  ev.getID(), ev.getWhen(),
                                                  ev.getModifiers(), ev.getX(),
                                                  ev.getY(),
                                                  ev.getClickCount(),
                                                  ev.isPopupTrigger(), path,
                                                  manager);
    processMenuDragMouseEvent(e);
  }

  /**
   * Process key events forwarded from MenuSelectionManager.
   *
   * @param event event forwarded from MenuSelectionManager
   * @param path path to the menu element from which event was generated
   * @param manager MenuSelectionManager for the current menu hierarchy
   */
  public void processKeyEvent(KeyEvent event, MenuElement[] path,
                              MenuSelectionManager manager)
  {
    MenuKeyEvent e = new MenuKeyEvent(event.getComponent(), event.getID(),
                                      event.getWhen(), event.getModifiers(),
                                      event.getKeyCode(), event.getKeyChar(),
                                      path, manager);
    processMenuKeyEvent(e);

    // Consume original key event, if the menu key event has been consumed.
    if (e.isConsumed())
      event.consume();
  }

  /**
   * This method fires MenuDragMouseEvents to registered listeners.
   * Different types of MenuDragMouseEvents are fired depending
   * on the observed mouse event.
   *
   * @param event Mouse
   */
  public void processMenuDragMouseEvent(MenuDragMouseEvent event)
  {
    switch (event.getID())
      {
      case MouseEvent.MOUSE_ENTERED:
        isDragging = false;
	fireMenuDragMouseEntered(event);
	break;
      case MouseEvent.MOUSE_EXITED:
        isDragging = false;
	fireMenuDragMouseExited(event);
	break;
      case MouseEvent.MOUSE_DRAGGED:
        isDragging = true;
	fireMenuDragMouseDragged(event);
	break;
      case MouseEvent.MOUSE_RELEASED:
        if (isDragging)
          fireMenuDragMouseReleased(event);
	break;
      }
  }

  /**
   * This method fires MenuKeyEvent to registered listeners.
   * Different types of MenuKeyEvents are fired depending
   * on the observed key event.
   *
   * @param event DOCUMENT ME!
   */
  public void processMenuKeyEvent(MenuKeyEvent event)
  {
    switch (event.getID())
    {
      case KeyEvent.KEY_PRESSED:
        fireMenuKeyPressed(event);
        break;
      case KeyEvent.KEY_RELEASED:
        fireMenuKeyReleased(event);
        break;
      case KeyEvent.KEY_TYPED:
        fireMenuKeyTyped(event);
        break;
      default:
        break;
    }
  }

  /**
   * Fires MenuDragMouseEvent to all of the menuItem's MouseInputListeners.
   *
   * @param event The event signifying that mouse entered menuItem while it was dragged
   */
  protected void fireMenuDragMouseEntered(MenuDragMouseEvent event)
  {
    EventListener[] ll = listenerList.getListeners(MenuDragMouseListener.class);

    for (int i = 0; i < ll.length; i++)
      ((MenuDragMouseListener) ll[i]).menuDragMouseEntered(event);
  }

  /**
   * Fires MenuDragMouseEvent to all of the menuItem's MouseInputListeners.
   *
   * @param event The event signifying that mouse has exited menu item, while it was dragged
   */
  protected void fireMenuDragMouseExited(MenuDragMouseEvent event)
  {
    EventListener[] ll = listenerList.getListeners(MenuDragMouseListener.class);

    for (int i = 0; i < ll.length; i++)
      ((MenuDragMouseListener) ll[i]).menuDragMouseExited(event);
  }

  /**
   * Fires MenuDragMouseEvent to all of the menuItem's MouseInputListeners.
   *
   * @param event The event signifying that mouse is being dragged over the menuItem
   */
  protected void fireMenuDragMouseDragged(MenuDragMouseEvent event)
  {
    EventListener[] ll = listenerList.getListeners(MenuDragMouseListener.class);

    for (int i = 0; i < ll.length; i++)
      ((MenuDragMouseListener) ll[i]).menuDragMouseDragged(event);
  }

  /**
   * This method fires a MenuDragMouseEvent to all the MenuItem's MouseInputListeners.
   *
   * @param event The event signifying that mouse was released while it was dragged over the menuItem
   */
  protected void fireMenuDragMouseReleased(MenuDragMouseEvent event)
  {
    EventListener[] ll = listenerList.getListeners(MenuDragMouseListener.class);

    for (int i = 0; i < ll.length; i++)
      ((MenuDragMouseListener) ll[i]).menuDragMouseReleased(event);
  }

  /**
   * This method fires a MenuKeyEvent to all the MenuItem's MenuKeyListeners.
   *
   * @param event The event signifying that key associated with this menu was pressed
   */
  protected void fireMenuKeyPressed(MenuKeyEvent event)
  {
    EventListener[] ll = listenerList.getListeners(MenuKeyListener.class);

    for (int i = 0; i < ll.length; i++)
      ((MenuKeyListener) ll[i]).menuKeyPressed(event);
  }

  /**
   * This method fires a MenuKeyEvent to all the MenuItem's MenuKeyListeners.
   *
   * @param event The event signifying that key associated with this menu was released
   */
  protected void fireMenuKeyReleased(MenuKeyEvent event)
  {
    EventListener[] ll = listenerList.getListeners(MenuKeyListener.class);

    for (int i = 0; i < ll.length; i++)
      ((MenuKeyListener) ll[i]).menuKeyTyped(event);
  }

  /**
   * This method fires a MenuKeyEvent to all the MenuItem's MenuKeyListeners.
   *
   * @param event The event signifying that key associated with this menu was typed.
   *        The key is typed when it was pressed and then released
   */
  protected void fireMenuKeyTyped(MenuKeyEvent event)
  {
    EventListener[] ll = listenerList.getListeners(MenuKeyListener.class);

    for (int i = 0; i < ll.length; i++)
      ((MenuKeyListener) ll[i]).menuKeyTyped(event);
  }

  /**
   * Method of the MenuElement interface.
   * This method is invoked by MenuSelectionManager when selection of
   * this menu item has changed. If this menu item was selected then
   * arm it's model, and disarm the model otherwise. The menu item
   * is considered to be selected, and thus highlighted when its model
   * is armed.
   *
   * @param changed indicates selection status of this menu item. If changed is
   * true then menu item is selected and deselected otherwise.
   */
  public void menuSelectionChanged(boolean changed)
  {
    Component parent = this.getParent();
    if (changed)
      {
	model.setArmed(true);

	if (parent != null && parent instanceof JPopupMenu)
	  ((JPopupMenu) parent).setSelected(this);
      }
    else
      {
	model.setArmed(false);

	if (parent != null && parent instanceof JPopupMenu)
	  ((JPopupMenu) parent).getSelectionModel().clearSelection();
      }
  }

  /**
   * Method of the MenuElement interface.
   *
   * @return $MenuElement[]$ Returns array of sub-components for this menu
   *         item. By default menuItem doesn't have any subcomponents and so
   *         empty array is returned instead.
   */
  public MenuElement[] getSubElements()
  {
    return new MenuElement[0];
  }

  /**
   * Returns reference to the component that will paint this menu item.
   *
   * @return $Component$ Component that will paint this menu item.
   *         Simply returns reference to this menu item.
   */
  public Component getComponent()
  {
    return this;
  }

  /**
   * Adds a MenuDragMouseListener to this menu item. When mouse
   * is dragged over the menu item the MenuDragMouseEvents will be
   * fired, and these listeners will be called.
   *
   * @param listener The new listener to add
   */
  public void addMenuDragMouseListener(MenuDragMouseListener listener)
  {
    listenerList.add(MenuDragMouseListener.class, listener);
  }

  /**
   * Removes a MenuDragMouseListener from the menuItem's listener list.
   *
   * @param listener The listener to remove
   */
  public void removeMenuDragMouseListener(MenuDragMouseListener listener)
  {
    listenerList.remove(MenuDragMouseListener.class, listener);
  }

  /**
   * Returns all added MenuDragMouseListener objects.
   *
   * @return an array of listeners
   *
   * @since 1.4
   */
  public MenuDragMouseListener[] getMenuDragMouseListeners()
  {
    return (MenuDragMouseListener[]) listenerList.getListeners(MenuDragMouseListener.class);
  }

  /**
   * Adds an MenuKeyListener to this menu item.  This listener will be
   * invoked when MenuKeyEvents will be fired by this menu item.
   *
   * @param listener The new listener to add
   */
  public void addMenuKeyListener(MenuKeyListener listener)
  {
    listenerList.add(MenuKeyListener.class, listener);
  }

  /**
   * Removes an MenuKeyListener from the menuItem's listener list.
   *
   * @param listener The listener to remove
   */
  public void removeMenuKeyListener(MenuKeyListener listener)
  {
    listenerList.remove(MenuKeyListener.class, listener);
  }

  /**
   * Returns all added MenuKeyListener objects.
   *
   * @return an array of listeners
   *
   * @since 1.4
   */
  public MenuKeyListener[] getMenuKeyListeners()
  {
    return (MenuKeyListener[]) listenerList.getListeners(MenuKeyListener.class);
  }

  /**
   * Returns a string describing the attributes for the <code>JMenuItem</code>
   * component, for use in debugging.  The return value is guaranteed to be 
   * non-<code>null</code>, but the format of the string may vary between
   * implementations.
   *
   * @return A string describing the attributes of the <code>JMenuItem</code>.
   */
  protected String paramString()
  {
    // calling super seems to be sufficient here...
    return super.paramString();
  }

  /**
   * Returns the object that provides accessibility features for this
   * <code>JMenuItem</code> component.
   *
   * @return The accessible context (an instance of 
   *     {@link AccessibleJMenuItem}).
   */
  public AccessibleContext getAccessibleContext()
  {
    if (accessibleContext == null)
      {
        AccessibleJMenuItem ctx = new AccessibleJMenuItem(); 
        addChangeListener(ctx);
        accessibleContext = ctx;
      }

    return accessibleContext;
  }

  /**
   * Provides the accessibility features for the <code>JMenuItem</code> 
   * component.
   * 
   * @see JMenuItem#getAccessibleContext()
   */
  protected class AccessibleJMenuItem extends AccessibleAbstractButton
    implements ChangeListener
  {
    private static final long serialVersionUID = 6748924232082076534L;

    private boolean armed;
    private boolean focusOwner;
    private boolean pressed;
    private boolean selected;

    /**
     * Creates a new <code>AccessibleJMenuItem</code> instance.
     */
    AccessibleJMenuItem()
    {
      //super(component);
    }

    /**
     * Receives notification when the menu item's state changes and fires
     * appropriate property change events to registered listeners.
     *
     * @param event the change event
     */
    public void stateChanged(ChangeEvent event)
    {
      // This is fired in all cases.
      firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY,
                         Boolean.FALSE, Boolean.TRUE);

      ButtonModel model = getModel();

      // Handle the armed property.
      if (model.isArmed())
        {
          if (! armed)
            {
              armed = true;
              firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
                                 AccessibleState.ARMED, null);
            }
        }
      else
        {
          if (armed)
            {
              armed = false;
              firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
                                 null, AccessibleState.ARMED);
            }
        }

      // Handle the pressed property.
      if (model.isPressed())
        {
          if (! pressed)
            {
              pressed = true;
              firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
                                 AccessibleState.PRESSED, null);
            }
        }
      else
        {
          if (pressed)
            {
              pressed = false;
              firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
                                 null, AccessibleState.PRESSED);
            }
        }

      // Handle the selected property.
      if (model.isSelected())
        {
          if (! selected)
            {
              selected = true;
              firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
                                 AccessibleState.SELECTED, null);
            }
        }
      else
        {
          if (selected)
            {
              selected = false;
              firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
                                 null, AccessibleState.SELECTED);
            }
        }

      // Handle the focusOwner property.
      if (isFocusOwner())
        {
          if (! focusOwner)
            {
              focusOwner = true;
              firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
                                 AccessibleState.FOCUSED, null);
            }
        }
      else
        {
          if (focusOwner)
            {
              focusOwner = false;
              firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
                                 null, AccessibleState.FOCUSED);
            }
        }
    }

    /**
     * Returns the accessible role for the <code>JMenuItem</code> component.
     *
     * @return {@link AccessibleRole#MENU_ITEM}.
     */
    public AccessibleRole getAccessibleRole()
    {
      return AccessibleRole.MENU_ITEM;
    }
  }

  /**
   * Returns <code>true</code> if the component is guaranteed to be painted
   * on top of others. This returns false by default and is overridden by
   * components like JMenuItem, JPopupMenu and JToolTip to return true for
   * added efficiency.
   *
   * @return <code>true</code> if the component is guaranteed to be painted
   *         on top of others
   */
  boolean onTop()
  {
    return SwingUtilities.getAncestorOfClass(JInternalFrame.class, this)
           == null;
  }

}
