package sq_gui

import swing.Component
import swing.SequentialContainer.Wrapper
import javax.swing.JPopupMenu

/**
 * Created by IntelliJ IDEA.
 * User: Kerstin
 * Date: 02.04.12
 * Time: 08:56
 * To change this template use File | Settings | File Templates.
 */

class PopupMenu extends Component with Wrapper {

    override lazy val peer: JPopupMenu = new JPopupMenu {
      def popupMenuWrapper = PopupMenu.this
    }

    def show(invoker: Component, x: Int, y: Int): Unit = peer.show(invoker.peer, x, y)

    /* Create any other peer methods here */
}
