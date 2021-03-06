package com.github.tadukoo.launcher;

import com.github.tadukoo.engine.FileDownloader;
import com.github.tadukoo.util.FileUtil;
import com.github.tadukoo.util.ListUtil;
import com.github.tadukoo.view.components.TadukooButton;
import com.github.tadukoo.util.tuple.Pair;
import com.github.tadukoo.view.font.FontResourceLoader;
import com.github.tadukoo.view.font.FontFamilies;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class LauncherMainFrame extends JFrame{
	private String genealogyAPIURL = "https://github.com/Tadukoo/TadukooGenealogy/releases/download/0.0.0.1-Pre-Alpha/GenealogyAPI-0.1-Alpha-SNAPSHOT.jar";
	private String genealogyProgramURL = "https://github.com/Tadukoo/TadukooGenealogy/releases/download/0.0.0.1-Pre-Alpha/GenealogyProgram-0.1-Alpha-SNAPSHOT.jar";
	private String fontZipURL = "https://github.com/Tadukooverse/TadukooFonts/releases/download/v.1.0/TadukooFonts.zip";
	
	private JFrame frame = this;
	private JPanel panel;
	private JTabbedPane tabbedPane;
	private JLabel label;
	private JComboBox<String> comboBox;
	private JButton button;
	private JButton updateButton;
	
	public LauncherMainFrame(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		URL iconURL = this.getClass().getResource("/img/Logo w-o Tagline.png");
		setIconImage(Toolkit.getDefaultToolkit().getImage(iconURL));
		
		panel = new JPanel();
		add(panel);
		
		/*
		AbstractForm newForm = new AbstractForm(new HashMap<>()){
			
			@Override
			public boolean labelsOnTop(){
				return false;
			}
			
			@Override
			public void setDefaultFields(){
				addField(StringFormField.builder().key("Test").defaultValue("Some Value").rowPos(1).colPos(0).rowSpan(1).colSpan(1).build());
				//addField(FormField.tableFieldBuilder().key("Test Map").rowPos(0).colPos(0).rowSpan(1).colSpan(1).build());
				addField(ButtonFormField.builder().key("Submit").rowPos(2).colPos(0).rowSpan(1).colSpan(1).build());
			}
		};
		panel.add(newForm);
		/**/
		
		/*
		label = new JLabel("Choose an app to run");
		panel.add(label);
		
		comboBox = new JComboBox<>(new String[]{"TadukooGenealogy.jar", "TadukooLookAndFeelTest.jar"});
		panel.add(comboBox);
		
		button = new JButton("Launch");
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				launchButton();
			}
		});
		panel.add(button);
		
		updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				// "Update"
				// TODO: If the engine/launcher itself needs updated, update includes an update application that
				// will close the actual engine/launcher, update the files, and re-open it (avoiding this garbage)
				try{
					Runtime.getRuntime().exec("TadukooLauncher.exe");
					System.exit(0);
				}catch(IOException ex){
					Launcher.logger.log(Level.SEVERE, "Failed to restart launcher", ex);
				}
			}
		});
		panel.add(updateButton);
		/**/
		
		updateButton = TadukooButton.builder()
				.text("Update")
				//.shapeFunc(Shapes.RECTANGLE_WITH_CUT_CORNERS_TR_BL.getShapeFunc())
				.build();
		updateButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				new FileDownloader(frame, ListUtil.createList(
						Pair.of(fontZipURL, Launcher.FONTS_FOLDER + "fonts.zip")));
				try{
					FileUtil.unzipFile(Launcher.FONTS_FOLDER + "fonts.zip", Launcher.FONTS_FOLDER);
					FontResourceLoader resourceLoader = new FontResourceLoader(true, Launcher.logger,
							GraphicsEnvironment.getLocalGraphicsEnvironment(), Launcher.FONTS_FOLDER);
					List<String> fonts = resourceLoader.loadFonts(ListUtil.createList(FontFamilies.ARIMO.getFamily(), FontFamilies.BANGERS.getFamily(),
							FontFamilies.CALADEA.getFamily(), FontFamilies.CALLIGRASERIF.getFamily(), FontFamilies.CARLITO.getFamily(),
							FontFamilies.COMIC_RELIEF.getFamily(), FontFamilies.COUSINE.getFamily(), FontFamilies.GELASIO.getFamily(),
							FontFamilies.LECKERLI_ONE.getFamily(), FontFamilies.LOBSTER.getFamily(), FontFamilies.ROBOTO.getFamily(),
							FontFamilies.ROBOTO_CONDENSED.getFamily(), FontFamilies.SATISFY.getFamily(), FontFamilies.SELAWIK.getFamily(),
							FontFamilies.SOURCE_CODE_PRO.getFamily(), FontFamilies.TINOS.getFamily(), FontFamilies.WINE_TAHOMA.getFamily()),
							true);
					Launcher.logger.logInfo("Loaded " + fonts.size() + " fonts");
					for(String font: fonts){
						Launcher.logger.logInfo("Font Loaded: " + font);
					}
				}catch(IOException | FontFormatException ioException){
					Launcher.logger.logError(ioException);
				}
			}
		});
		panel.add(updateButton);
		
		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);
		
		tabbedPane = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
		
		layout.putConstraint(SpringLayout.NORTH, panel,
							5,
								SpringLayout.NORTH, tabbedPane);
		layout.putConstraint(SpringLayout.SOUTH, panel,
								5,
									SpringLayout.SOUTH, tabbedPane);
		layout.putConstraint(SpringLayout.WEST, panel,
								5,
									SpringLayout.WEST, tabbedPane);
		layout.putConstraint(SpringLayout.EAST, panel,
								5,
									SpringLayout.EAST, tabbedPane);
		SpringLayout.Constraints cons = layout.getConstraints(panel);
		cons.setWidth(Spring.constant(800));
		cons.setHeight(Spring.constant(600));
		SpringLayout.Constraints tabCons = layout.getConstraints(tabbedPane);
		tabCons.setWidth(cons.getWidth());
		tabCons.setHeight(cons.getHeight());
		
		createTabComponents("Tadukoo Genealogy", "It's about genealogy", 0);
		createTabComponents("Tadukoo Look & Feel Test", "It's a look and feel test", 1);
		createTabComponents("Stratego", "Play Stratego", 2);
		createTabComponents("Tadukoo Pojo Maker", "Make Pojos", 3);
		
		panel.add(tabbedPane);
		
		pack();
	}
	
	private void createTabComponents(String title, String description, int index){
		ProgramInfo programInfo = new ProgramInfo(title, description);
		tabbedPane.addTab(title, programInfo);
		
		/*
		AbstractForm form = new AbstractForm(){
			@Override
			public void setDefaultFields(){
				addField(FormField.stringFieldBuilder().key("Title").defaultValue(title).rowPos(0).colPos(0).isTitle(true).includeLabel(false).build());
				addField(FormField.stringFieldBuilder().key("Description").defaultValue(description).rowPos(1).colPos(0).includeLabel(false).build());
				addField(FormField.buttonFieldBuilder().key("Launch").rowPos(2).colPos(0).actionListener(e -> launchButton(title)).build());
			}
		};
		tabbedPane.addTab(title, form);
		/*
		
		// Make the tab content
		JPanel tabContentPanel = new JPanel();
		tabContentPanel.add(new JLabel(title));
		JTextField descriptionField = new JTextField(description);
		descriptionField.setEditable(false);
		tabContentPanel.add(descriptionField);
		JButton launchButton = new JButton("Launch");
		launchButton.addActionListener(e -> launchButton(title));
		tabContentPanel.add(launchButton);
		tabbedPane.addTab(title, tabContentPanel);
		/**/
		
		// Make the tab info
		JPanel tabPanel = new JPanel();
		JLabel titleLabel = new JLabel(title);
		tabPanel.add(titleLabel);
		tabbedPane.setTabComponentAt(index, tabPanel);
	}
	
	/*
	private void launchButton(String option){
		//String option = (String) comboBox.getSelectedItem();
		if("Tadukoo Genealogy".equalsIgnoreCase(option)){
			new FileDownloader(this, Arrays.asList(
					Pair.of(genealogyAPIURL, Launcher.LIBS_FOLDER + "GenealogyAPI.jar"),
					Pair.of(genealogyProgramURL, Launcher.PROGRAMS_FOLDER + "TadukooGenealogy.jar")));
			runProgram("TadukooGenealogy.jar", Collections.singletonList("GenealogyAPI.jar"));
		}else if("Tadukoo Look & Feel Test".equalsIgnoreCase(option)){
			// TODO: File Downloader
			runProgram("TadukooLookAndFeelTest.jar", Collections.emptyList());
		}else if("Stratego".equalsIgnoreCase(option)){
			// TODO: File Downloader
			runProgram("Stratego.jar", Collections.emptyList());
		}
	}
	
	private void runProgram(String programFileName, List<String> dependencyFileNames){
		List<File> files = new ArrayList<>();
		files.add(new File(Launcher.PROGRAMS_FOLDER + programFileName));
		dependencyFileNames.forEach(fileName -> files.add(new File(Launcher.LIBS_FOLDER + fileName)));
		
		List<URL> urls = new ArrayList<>();
		List<String> classes = new ArrayList<>();
		for(File file: files){
			try{
				urls.add(new URL("jar:file:" + file.getPath() + "!/"));
				JarFile jarFile = new JarFile(file);
				jarFile.stream().forEach(jarEntry -> {
					String name = jarEntry.getName();
					if(name.endsWith(".class")){
						classes.add(name);
					}
				});
			}catch(IOException e){
				Launcher.logger.logError("Failed to load file: " + Launcher.LIBS_FOLDER + file.getName(), e);
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				JOptionPane.showMessageDialog(this, sw.toString());
				return;
			}
		}
		
		URLClassLoader jarLoader = new URLClassLoader(urls.toArray(new URL[0]));
		Class<?> programClass = null;
		try{
			for(String className: classes){
				Class<?> clazz = jarLoader.loadClass(className.replaceAll("/", ".").replace(".class", ""));
				Class<?>[] interfaces = clazz.getInterfaces();
				for(Class<?> anInterface : interfaces){
					if(anInterface == Program.class){
						programClass = clazz;
						break;
					}
				}
			}
		}catch(ClassNotFoundException e){
			Launcher.logger.logError("Failed to load classes", e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			JOptionPane.showMessageDialog(this, sw.toString());
			return;
		}
		
		if(programClass == null){
			Launcher.logger.logError("Did not find a program class");
			return;
		}
		
		try{
			Program plugin = (Program) programClass.getDeclaredConstructor().newInstance();
			if(plugin.load()){
				System.out.println("Loaded program " + plugin.getClass().getCanonicalName() + " successfully");
				plugin.run();
			}
		}catch(InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e){
			Launcher.logger.logError("Failed to load " + programClass.getCanonicalName() + " program", e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			JOptionPane.showMessageDialog(this, sw.toString());
		}
	}
	/**/
}
