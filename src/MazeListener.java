import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeListener implements ActionListener{

	private MazePanel panel;
	public MazeListener(MazePanel panel) {
		this.panel = panel;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		panel.playPressed();
	}

}
