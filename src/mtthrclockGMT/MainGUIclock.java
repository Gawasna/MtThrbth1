package mtthrclockGMT;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.time.*;
import java.time.format.*;

public class MainGUIclock extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> GMTbox;
    private static ZoneId currentZoneId ;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainGUIclock frame = new MainGUIclock(currentZoneId);
                    frame.setVisible(true);
                    ClockFrame defaultClock = new ClockFrame(currentZoneId);
                    defaultClock.setTitle("Current Time");
                    defaultClock.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainGUIclock(ZoneId currentZoneId) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 257, 149);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        String[] countries = {"UTC-12: Baker Island, Howland Island", "UTC-11: American Samoa, Niue", "UTC-10: Hawaii (Hawaii-Aleutian Time)", "UTC-9: Alaska (Alaska Time)", "UTC-8: United States (Pacific Time)", "UTC-7: Mexico (Mountain Time)", "UTC-6: Canada (Central Time)", "UTC-5: United States (Eastern Time)", "UTC-4: Canada (Atlantic Time)", "UTC-3: Argentina (Argentina Time)", "UTC-2: Brazil (Fernando de Noronha Time)", "UTC-1: Portugal (Azores Time)", "UTC+0: United Kingdom (Greenwich Mean Time)", "UTC+1: France (Central European Time)", "UTC+2: Egypt (Eastern European Time)", "UTC+3: Russia (Moscow Time)", "UTC+4: United Arab Emirates (Gulf Standard Time)", "UTC+5: Pakistan (Pakistan Standard Time)", "UTC+6: Kazakhstan (East Kazakhstan Time)", "UTC+7: Thailand (Indochina Time)", "UTC+8: China (China Standard Time)", "UTC+9: Japan (Japan Standard Time)", "UTC+10: Australia (Eastern Standard Time)", "UTC+11: Solomon Islands (Solomon Islands Time)", "UTC+12: New Zealand (New Zealand Standard Time)", "UTC+13: Tonga (Tonga Time)", "UTC+14: Kiribati (Line Islands Time)"};
        GMTbox = new JComboBox<>(countries);
        GMTbox.setBounds(10, 84, 154, 21);
        contentPane.add(GMTbox);
        GMTbox.setSelectedIndex(19);

        JLabel lblNewLabel = new JLabel("Chọn múi giờ");
        lblNewLabel.setBounds(10, 61, 75, 13);
        contentPane.add(lblNewLabel);

        JButton btnNewButton = new JButton("Mở");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openNewClockFrame();
            }
        });
        btnNewButton.setBounds(174, 61, 58, 44);
        contentPane.add(btnNewButton);
        
        JLabel currenttime = new JLabel("Giờ hiện tại");
        currenttime.setBounds(10, 21, 75, 13);
        contentPane.add(currenttime);
        
        JLabel timelabel = new JLabel("");
        timelabel.setBounds(10, 38, 75, 13);
        contentPane.add(timelabel);

        GMTbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateCurrentTimeZone();
                }
            }
        });

        updateCurrentTimeZone();
    }

	private synchronized void updateCurrentTimeZone() {
        String selectedCountry = (String) GMTbox.getSelectedItem();
        currentZoneId = ZoneId.of(getTimeZoneFromCountry(selectedCountry));
    }

    private synchronized void openNewClockFrame() {
        ClockFrame newFrame = new ClockFrame(currentZoneId);
        newFrame.setTitle((String) GMTbox.getSelectedItem());
        newFrame.setVisible(true);
    }

    private String getTimeZoneFromCountry(String selectedCountry) {
        String[] parts = selectedCountry.split(": ");
        String timeZone = parts[0];
        return timeZone.replace("UTC", "GMT");
    }
}

class ClockFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JLabel timelabel;
    private ZoneId zoneId;

    public ClockFrame(ZoneId zoneId) {
        this.zoneId = zoneId;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 257, 100);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel currenttime = new JLabel("Giờ Hiện Tại");
        currenttime.setBounds(10, 10, 98, 21);
        contentPane.add(currenttime);

        timelabel = new JLabel("");
        timelabel.setBounds(10, 30, 200, 21);
        contentPane.add(timelabel);

        startClockThread();
    }
    public synchronized void startClockThread() {
        Thread clockThread = new Thread(() -> {
            while (true) {
                ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
                String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                synchronized (timelabel) {
                    timelabel.setText(formattedTime);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        clockThread.start();
    }
}
