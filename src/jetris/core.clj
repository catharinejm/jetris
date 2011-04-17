(ns jetris.core
  (:use [clojure.java.io :only (as-file)])
  (:import [java.awt GraphicsEnvironment GraphicsDevice GraphicsConfiguration Graphics2D]
	   [javax.swing JWindow JPanel JFrame KeyStroke AbstractAction]
	   [java.awt.image BufferedImage]
	   [java.awt.event KeyEvent]
	   [javax.imageio ImageIO]))

(def gfx-env (GraphicsEnvironment/getLocalGraphicsEnvironment))
(def gfx-device (.getDefaultScreenDevice gfx-env))
(def gfx-config (.getDefaultConfiguration gfx-device))

(def sprite (atom
	     {:x 0	     
	      :y 0
	      :image (ImageIO/read (as-file "resources/jetris_piece.gif"))}))

(def pane
  (proxy [JPanel] []
    (paint [gfx]
	   (.drawImage gfx
		       (:image @sprite)
		       (:x @sprite)
		       (:y @sprite)
		       nil))))
(def frame (JFrame.))

(.put (.getActionMap pane)
      "space"
      (proxy [AbstractAction] []
	  (actionPerformed [e]
			   (swap! sprite update-in [:x] inc)
			   (.repaint frame))))
(.put (.getInputMap pane)
      (KeyStroke/getKeyStroke KeyEvent/VK_SPACE 0)
      "space")

(doto frame (.add pane) (.setSize 640 480) (.setVisible true))
