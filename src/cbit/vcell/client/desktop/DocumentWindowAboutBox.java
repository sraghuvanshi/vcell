/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.client.desktop;
import java.awt.*;
import javax.swing.*;
/**
 * This type was generated by a SmartGuide.
 */
public class DocumentWindowAboutBox extends JDialog {
	private JLabel ivjAppName = null;
	private JPanel ivjButtonPane = null;
	private JLabel ivjCopyright = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JLabel ivjIconLabel = null;
	private JPanel ivjIconPane = null;
	private JPanel ivjJDialogContentPane = null;
	private JButton ivjOkButton = null;
	private JPanel ivjTextPane = null;
	private GridLayout ivjTextPaneGridLayout = null;
	private JLabel ivjUserName = null;
	private JLabel ivjVersion = null;
	public static String BUILD_NO = "";
	private JLabel ivjBuildNumber = null;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == DocumentWindowAboutBox.this.getOkButton()) 
				connEtoM1(e);
		};
	};

/**
 * DocumentWindowAboutBox constructor comment.
 */
public DocumentWindowAboutBox() {
	super();
	initialize();
}

/**
 * connEtoM1:  (OkButton.action.actionPerformed(java.awt.event.ActionEvent) --> DocumentWindowAboutBox.dispose()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.dispose();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * Return the AppName property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getAppName() {
	if (ivjAppName == null) {
		try {
			ivjAppName = new javax.swing.JLabel();
			ivjAppName.setName("AppName");
			ivjAppName.setText("Virtual Cell");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAppName;
}

/**
 * Return the Spacer property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JLabel getBuildNumber() {
	if (ivjBuildNumber == null) {
		try {
			ivjBuildNumber = new javax.swing.JLabel();
			ivjBuildNumber.setName("BuildNumber");
			ivjBuildNumber.setText("");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBuildNumber;
}

/**
 * Return the ButtonPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getButtonPane() {
	if (ivjButtonPane == null) {
		try {
			ivjButtonPane = new javax.swing.JPanel();
			ivjButtonPane.setName("ButtonPane");
			ivjButtonPane.setLayout(new java.awt.FlowLayout());
			getButtonPane().add(getOkButton(), getOkButton().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjButtonPane;
}


/**
 * Return the Copyright property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getCopyright() {
	if (ivjCopyright == null) {
		try {
			ivjCopyright = new javax.swing.JLabel();
			ivjCopyright.setName("Copyright");
			ivjCopyright.setText("(c) Copyright 2004");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCopyright;
}


/**
 * Return the IconLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getIconLabel() {
	if (ivjIconLabel == null) {
		try {
			ivjIconLabel = new javax.swing.JLabel();
			ivjIconLabel.setName("IconLabel");
			ivjIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ccam_sm_colorgr.gif")));
			ivjIconLabel.setText("");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjIconLabel;
}

/**
 * Return the IconPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getIconPane() {
	if (ivjIconPane == null) {
		try {
			ivjIconPane = new javax.swing.JPanel();
			ivjIconPane.setName("IconPane");
			ivjIconPane.setLayout(new java.awt.FlowLayout());
			getIconPane().add(getIconLabel(), getIconLabel().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjIconPane;
}


/**
 * Return the JDialogContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJDialogContentPane() {
	if (ivjJDialogContentPane == null) {
		try {
			ivjJDialogContentPane = new javax.swing.JPanel();
			ivjJDialogContentPane.setName("JDialogContentPane");
			ivjJDialogContentPane.setLayout(new java.awt.BorderLayout());
			getJDialogContentPane().add(getButtonPane(), "South");
			getJDialogContentPane().add(getTextPane(), "Center");
			getJDialogContentPane().add(getIconPane(), "West");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJDialogContentPane;
}


/**
 * Return the OkButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getOkButton() {
	if (ivjOkButton == null) {
		try {
			ivjOkButton = new javax.swing.JButton();
			ivjOkButton.setName("OkButton");
			ivjOkButton.setText("OK");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOkButton;
}


/**
 * Return the TextPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getTextPane() {
	if (ivjTextPane == null) {
		try {
			ivjTextPane = new javax.swing.JPanel();
			ivjTextPane.setName("TextPane");
			ivjTextPane.setLayout(getTextPaneGridLayout());
			getTextPane().add(getAppName(), getAppName().getName());
			getTextPane().add(getVersion(), getVersion().getName());
			getTextPane().add(getBuildNumber(), getBuildNumber().getName());
			getTextPane().add(getCopyright(), getCopyright().getName());
			getTextPane().add(getUserName(), getUserName().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTextPane;
}

/**
 * Return the TextPaneGridLayout property value.
 * @return java.awt.GridLayout
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.GridLayout getTextPaneGridLayout() {
	java.awt.GridLayout ivjTextPaneGridLayout = null;
	try {
		/* Create part */
		ivjTextPaneGridLayout = new java.awt.GridLayout(5, 1);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjTextPaneGridLayout;
}


/**
 * Return the UserName property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getUserName() {
	if (ivjUserName == null) {
		try {
			ivjUserName = new javax.swing.JLabel();
			ivjUserName.setName("UserName");
			ivjUserName.setText("UCHC / NRCAM");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUserName;
}

/**
 * Return the Version property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JLabel getVersion() {
	if (ivjVersion == null) {
		try {
			ivjVersion = new javax.swing.JLabel();
			ivjVersion.setName("Version");
			ivjVersion.setText("Version 4.0");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVersion;
}

/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	exception.printStackTrace(System.out);
}


/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getOkButton().addActionListener(ivjEventHandler);
}


/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("DocumentWindowAboutBox");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(330, 160);
		setTitle("DocumentWindowAboutBox");
		setContentPane(getJDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}

/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		DocumentWindowAboutBox aDocumentWindowAboutBox;
		aDocumentWindowAboutBox = new DocumentWindowAboutBox();
		aDocumentWindowAboutBox.setModal(true);
		aDocumentWindowAboutBox.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aDocumentWindowAboutBox.show();
		java.awt.Insets insets = aDocumentWindowAboutBox.getInsets();
		aDocumentWindowAboutBox.setSize(aDocumentWindowAboutBox.getWidth() + insets.left + insets.right, aDocumentWindowAboutBox.getHeight() + insets.top + insets.bottom);
		aDocumentWindowAboutBox.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JDialog");
		exception.printStackTrace(System.out);
	}
}


/**
 * 
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private static void getBuilderData() {
/*V1.1
**start of data**
	D0CB838494G88G88GC6FBB0B6GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BB8DD4D4D7FAF423FBC80EBD21DB32A19E36F50D49312DC9EC57ED6C2E5BF5D332350DDB8DA12D69EA0F6704CDF5135D43595854CD6959472082910D3FD93312E6E2B4E24481911582A2B0434060G83B38C435F42E3668103EF660DB38FC1E3CC3F6F5EF75FBC06B720D6B967BB6F5D6F3B77FE77FEFF777B6EBC38C39FAF5A19E3B844F1B9BBB9656FE28E47ED2E6438B776147E0E61AACF2697F01A
	3FCFG76F3DB7DBB60598C343A417402FD5C77FB72215D8F6D340174025F427BC1EEF5CF7F85F809432782E8377F7E73145979246DE4FC72C87B5D06C2F86E8598829CBE171DC47E874CC7947CBC854FA04740F1C94558670C39C841E5C11FFC405981060AE97FA9F852854C2F2AD47A757F51D2EE5BBF068DFBF09E4D1C8CB6A8E333152711FBB7E749A96465B697AAE3AA20AD826066F3B96BD7A8129A475551DA6F30DFB6DD778A22E8720AFEA1A81BFCC2642AAC052A2AAC12F7A2G18EEFF50A7CD568FCB93
	F203B48573FF0EF37F09B91F4C45F1DBE05EE5847701977979607D96C072678C3F09603360BD7FF4FA0171B45B632513B54A9EBCDFBC4FE51487F63C9FE70F1527695E06CEC777189058E4781068E3C79E4ABD95E82B81B2G36839CA9C9AF2884B8CA6CE0772E4F61D9BFAC1B25C0C08A5607C28D22AF5CAA7984915E2B2A002260BA852F9476F19C55F53F39C4E7BD33819DB7FE576328CC161EE3FADF5679AC3771BB5FB4646948E46352AF8C0E92AA0B311228CCA86FA76372CEAC19EE1753F9F7F3DA5EBB76
	7CF36ECE9D5E46F7DF4DCDD566D92E613D3984F57D885175FAF84FD16C036213897E7819A81E5AE89141E33B82E8A9E718EDF876323558F7AC66761DFDDCD78EA9AFACB0E41F21EBB8E40ADDCB078F672EB3452E250F609D2658353C2E2EE588E8E9G543E7AEB51E70BCB538BACG7DGB2C0B240F20034D2E6E32EDD25F7E8E38EBEACB7CC78C51F90666858BDF99F40D31812433CAD98117920D7F073E1BFBFAC8A91CE6973C19E4AE0F85A382344763D4023C9888AE1DE76CBC1181D8F8832900E50BE477376
	E31F30C028AEFFC0G8601904766AD49BB88CF110F48DDA19FAF8B519E14DE1107BA394E3BE1C618G5E29DC9E3E0C724A047DE7839416B1B9BCC7663BA2040302D8D5E517242B93A1976E046BAFC3B9FF0F448EA33CD716B1994F2FC35CA2E82FD467F9A36F00E69E0B5F0B4B6743B7180FA5F9B1AE2FADE3B673CB8F33994B1EE73812D79F533519E4305F2CB2EAAB79E7E3FDE7A1199357C55B473C9E9DFFD9DB266FC7F78B14C735AE0F5C7BB07FDED5D7EF57232E4AE03FF6G71AC1371A779C6EA338DFCC4
	E0D65A9E868393FCD10953F5F67BF6A87224ED1E34671DE5325ACA7621973F6F1F2C8CBE4FFD14CDACD0190C5462B9A4DC07434FA20404208FDEA28AADD00B3626786908240E0990FC887C5A8C0E94D171B7EA5177F8D98E7B07A7E441A94832BFB89A257F81619519988EC5C3826315D80B71D71A843FD67B7FE1AD7A04D78A8685229CF508C2FF147824A08AF8F41B250920CC710FDDC6DDDD95EEA8B161094B3BB874E9F182638B4D8B16733BF18FE1FFGD40EA10A634A51DFDFD6739AF2B6003EB13F490067
	16334C77A6BFE53EF77C661FF12B87C3863D33A36529C78D4D6554F77A4B233EC75774C81D7E5A13C8ECD070913A564FC83C0C1723D01BAFA1794FAD60138430341C59FCE1BD52D9DCD10C5DAC09A29529AA6BDF576FC35D8857A684884798402E2872639A8E44505C0AACA9BD3F81E5A9EAE21F639C4A729BBE167F2D0675ECG58DECE63E065B9AA8F3A3F4EEB38C612230D735779A9D3E4926C262A2A456267C5E914E3F9D83585E503BAC8BD47722DACAB6EBB835AEF816C28E0BAFAF98F4B37122FAE65B66F
	F9B5B7DECED9D4C157D2D79155514667A6F329DC2F931E89C0CB2AE0B2FD28C19B736BA391496BA7470DA20FF233D60794F7517C9EBCC77C6108EC83A71BE2323BE4467562F924201573797BBEF6BEAF813E9B81647308CFE8BD24D97BA14055G8C816412790E89656803C439A67AF0886191A99C90FC45A8DD93BFA91B083F1B6A8939915FCFB1DDBC7DDBDC274F9F89C9912178141B65CB4F7CCF1D42F36D330B39FCFE3EC1AFFFDD30F554306ABC15E366F90DBCB3B69932GE8DC2B33E04ED6F281EA198BB3
	4F96D78CEE8C5AB41771B5919B3F901B638489FE4105589C679DAB13E1AA50380B6C7C4D35220F7B201DG1082309CE0BDC056C52663974D6738B9729BDB90520EA0AFBA2785B4C52FDFD8F2E98CD25ADFD9F632984431787EA35FDCF9F3E165F4D950F16B669CD796B30E6A76FDE79CBFEB1810E5928BE88CFC33C59B831BE1AE91A089E089G754567DB7315F9D7D5B21F7B522A7539D56FFFE9589D476736A96BBB743FD19BC95B7A0E62F34FB4E15CF081AD8410883084E08DC0A6C0FE9553D1FEE375DCBAA2
	466D1228251BE3CF92A5C76BA6BEDE8C735A81A86ED9029B0276140A6BEEC45C82E8AFD1F1D3D6442D017646AA169B0CB7617933C5ED4B240DB2246DEC2BDE8E747F8164B55DC68EB75DEF9E5ABCC4B9DB2C71F30703D67D5C61C32BFE6E7031F5BF45439C5A0A0151CFDAE74FADCA2DFA3945C7E45D7FAE9F66F47208B6A89C14FD9D64367B8A14BE0D3254FAD6751937CF9D517799878F511F7A4BD3CD317E02E7DCFEF5FAC1D1B57317CF2FB0FF2928E6E757CBF60CCFA234678124D6B37BD8552A675FAB2A29
	0E57D44F0C1954EFAAECF8B6EC847AF600FDB5698545G4DB5EACC334E99539A27E609E94FB74557775FB6696BBB2D894F5D08B491768A31BAFD09480D52223EE91305G2A067649A063E5BEBCAA4C30091F12711496EFFCE6D3BC3B21FA8FDA50B6FC4288BFA102C94221579ED2CACB40AF2AC1BDBDECE13A9B2BE1676C0B965CEFD80878EF120C07755DC47AE24C4B2CE1F7D0FFB909F1F0848AD5E06D8C89DEEE41A5F2EEB5B23B598EFD8B81D782E08790AFB1BE0FDAF18E59AF939E9917704E4E626054751F
	F0E3EE26E4CAA0841978CFD0E0B9539FB733359F29E5F6D75EC26E8420BD8610D04B6C2E78CA2C5D2554C6EDEDD96DCC3B23676F65D61CEFF5ED6C39ECA37875353167724BF6F6AEEF861A65B20B252F10B3E5A934D72A384AB696F353AEB3BB2E314537C333CD5F8EEDB67D3853EE1BBD6EF45BE6371FC5A417510486934D4FC195F833A1C7CC961B53516EEC9CECEF9BB45B61051E150BA635F71E156FFDCD3706A0317D32F267491E079766D05E1D0DB8E60497A3E89BD45F490D4C7EAA6A400E6A9077C223B6
	E744317550410431075037CD2363FEA19D5450BF33EA69387E478CDA3AE79AFD27E6BC5D6BCBA7D8DE107D52F2CE7C6E3C38773BAB6A689EB7A84F244CA7F2D2FEF3B3174E332ECFAB33FD8BC5C3CE1CF976555371C57531F79EA94D31B66E280F5A352FBE1E0DBFCDFCE62A1E5AF21E0A7FAB02CF5260290DD792BC3617822D30015973E5F28E0420BDCF45A558190DAFEDE03E58580AB9C3065AEEA7ED2C21E87BDF6C777BFC5F2B700B6690D98F04C72E66F9107BB959178C8ECBE3DBFBA7EAFACB834A253D6D
	48AC7350B3AF3B93E5D7E60638E9E67161E147E9F83A79F16504EB7479E5A9CC0EB8C0BA4292C4F879864512CAD6596F930D77F60F7D23BCE65FAB3E77675C4A2FDB62663D22194AF70172ACFE7D091C68DEC6DC385FD5C05BG108DF04882E7B8C01D056DED3D835708A7B5AC3FDE16F96F9829B259816E768B13D1B49EB64A7DE5972ED439DB6DB891CFBEB0C38B44327661F1B8CCDDBC565E54C61FEBE6F66B32B03B7D8F9B335BA98B334BE5446ED3213D82E00D0ADFC970D82BEE849C57487035ADE8E3EB14
	7E71641CBA8BEDB6D06E72888FFC5FF987F6F9AF76FC107BF1B35EB1097C8DE9C2069A359165B3F605596FF1E857B452BD789AE94D3E0174F95CEE1CE56EB4E58C657154D114F77BD5DA2ED15BDBB3200D2D46536D3A31F57B40CD0351CA6732D8B5B557B5E3AE35099CA76254F30E3A853B5FF1E2FC77877DB2E499106FE0F264040E41D1EADBBFF5A3FD8A66CD86D8893096E03315596C8267B1EA33BA765A689B9558D94978C92E78677BF5575C3F9DFC6542BB0CE1892494407999FE3E8BEDC016C284295C43
	39C89E679F9D13E9D7851F64C2D90B4288C1D3FDEEEAE67A4C063D9DEA22F22CE92275312B897B94364C9633289E3B5E4775D1AD52DC0D628F1DC61EAE589CE2A9EE719462ECDE25A729B7465B746A73D4603F9C007679EE37DE1FB5C05F88C04F2824AB6CB753ED00ABE9E6796A3972E97784F8E3D4D5E59525C93BE25E9C4DA3FFC2467BE05CAD0064E6F68FB075993349E5CFAF66B28A746F5EB63B3EB4ACEB26B2DC5F9C35496440A6835D7B8ABB2E4789AC47B896EBA785B6F751DFBC4995FDDCE7582FB3B7
	C2210D4ED9E90B4E9D0D35ABBCA89F91E809G4BG52GB6GE459185D3E67AE66621FA3D49844F6433C1F5D5F3E6E0EEF3BBFF74FED3BEF3B495DA1581C6BC6C829E1363BB136C8A3A391C175117FF66FD3F13608C30A7851AB29DEF75A18DEF734C02CE9E1FAFD43F4F426DE9BA43C93D6B4CB755AC0464BB0EE814012962657712BDA5923BC6365F12BDA284CB737CC17BD4DD19737311A44F80555A4B7BB1127875AA10044AB2CEE7EC5B76B3BC86D1B5C03FDB7C0BB8BA05F4E4E12EF4E28DF6E6CFCD8F257
	63E86ED7CF6A98231D66FEB9AA3E0560CBB4F82A17D7D4FF2B84DA2A1D5DC7FAFA519639567402A2008AGBB008810584AEC719B5DE55C9C353BCB8A594170C4CD992358FA1F911E29B057CA000DG5BG725BC046G95G76B64667356E59EC1E9ACB2C4D7F40935F6653BCF35B7CCFBC7A717A954F4CF83D41239F2F7F4D230D57D42F3BED77E68F9F5E75B82A773FA6FEB35696EB8FBF247E5496EB8F4D2A1FA6GAD330D59C351G5AC3F3FBFA018FA091E091408A00356DCCCF57FA4E4D2227CEC9A2E788DE
	88001E34AAD3763847913B474D6D519CEBDBFB3CBA681744CF739DF4AF3BD5FCB64197EB70F40F89840F6DE620ADF3B0DF7DD5F7AC7FFD9DD11E47BB62714F6F4579AABBE26F9A8E90FCF3C76CDD43429EF65760815A2A8E463F494E297AF6E85EDF3B72A0EA1E6976752B874A035A875F9B5FA747FA756FC8023D7AF7A4F26F6CF72F37FA637F2E7BD06F4C5FF5FF5897FFEDBF6E1BBBDE2C6B530F972F764D0C977F5A279FAFB677E96385457DCC19B72CDE8ED37C1BFDF84E515C3E158F7223E4AF4ADD2DADEA
	BBAFEA5E5BEC8F5E0EC69FA88F3A77938371F5D5B2B0372E4E8F686B2AFAE0262E6A8674F5E59D506A0A561E479D2C9EB5F632FA343413552316CE66675BBA31B689C1FB9EC08A40F200B5AA5DC0EA1B0D504E86A8F2C23E84506FE4742FF76A49986743673CCE7D3AF4F98CFE0DCE3F94F58DFF2A4B03552371EA560218766EB8F6C2E5D360E1723A65E472EA69E172DA246EF7EFAF794EC8ED9FA16DCC353D2867DEEC8E673BF33B7E1BFB62F12775BB2D65DA9D2CBE5A61823D83D8GBCAEFABFC1EBF3EB27F1
	16F9C2AEEDED7E7BCADCBB1C3FA4F0D05C07D64431AF49A86EBF8F231EC810EC1B888CC3A421F8FE8875E916C2B7087D2B735E8E200E3AA2B4A251EFGAEBA35772D7314B5AFF531EF84BEF26951D729745CAEBD7AFA153ED5171E2952573975685B61C9E5DB6EE132AD6C0A5EFB9C6F2277BED436FC4FEC322D69524AB61D5C7FE28DEC670785915E69BAEC3D316B40796DDD2C3E1DF0307CEA88F0A9DD2C8EDAB728D35FB6057DBE353E2535478F9C2C76D85F45EA0FDB9DA47FF1C35C8128F4335FE93C1DD823
	19857CA6C07D6DF0A44EEF07E49B14C75049F8B85C2C66F13998AED145D5F5B15CEA95E7F4B35CF69537CB2D174A3A199CFABDCC8EE300DB544D7672B841EF00F696C03E076D65F68F7AFC379011691C893D790AEF9ABDAC8EFCB1A3FF3CB35FFE789E473DB0ED1C47B3FBBC91BD6C1B11AC222F84CFECCE7EEB02DF60094D493F5641E4158234ECF53F91275E3A07FA68BA641E196B2163FADCFA6392157E29F147DD6E529B37DC691F96F75C89375E388DCA7F37620E5BBDA39FC760FA156FB4FA630D9B725C1B
	BE03F7BD0E6AEDB749670BFBE375F90860ABFAE37579AD55766B0036241769F3FEAF724F6F1B3E2FA225DD56B7F33F744CFC0D44848B50DDFD6C9C7D2483F1E3503E554766FF2703D3777A9B4DFBE247034F8B3F7DC0F950BDBF62E43F55A629F2F853497209D49597F231FE2BD55C4BAE56EF2D0AF3F731FED9AA6E79AE56AFDB455538D9BFE3BF43FD4B4D7A152838E35D2C1FC345BD544D7A7577B3BD6D6F096A66F71A77A7BD8FDECF2B9EA88F3A67D4923B93E03FC9D1B98CB359ACD1F11943CCB64B7B59B9
	106540F3A0ACCDC668378D3B081DE37E122176296C403DF8A5F1A2900CA85FC034F7307F174904FE74F73D553FC759525F758C2E596E48E97F5787237D375CC17FCABE5A7F1F3A666E5FB79C6D3F42BDF77F5B5EE87F055DF35F71FF07644DFE48D928CC9EA5B54CB61087B740E472EDDFF44E7F9A1EFB4E0FBAB00E1DD75BA324FDD6DD63C97C39CE2109437811C8D018B4EA3FCD308520F845C42A98BF1941A23F781489DEBB857A95CE7131D2BFF60914FA3DFCE0B09298844DCA615130E954BFF26C54A97AFF
	E676B18998AF7B8537D366BE507FACD9668C6DD7E42E506D8F4B933CE8B48B22A8F33B15DC58D8E1AA03B4CB668E94FBCF99552457D8DED6D681033A4C4DE6E329312D53DC5F8A5DA2E16ED32F94B079078326096B8193BFAC1B841FDFB6E1C66CD337547BF7C3AB477E6170B1833753466D9E718B224FC9B6E567761116C508F8437E905E1383E7BB4A25D46403232574372CEABB371F74E34240692FF1373982BBF7D0B31D55AF8A41A8E92F065455E917393DF46620A017C23BFA8A7F77A4C7660E76D349766B
	C8962833489D999506145FBFCEFD257D86E27BFCEF5C6FC6668D52B3ACE5B0FA87CF6F97B70F23ADE6GBEEB105DA7160D1F61627FA66C8C7020BFDE3CDA9F74616F438DB7E4A1FA7F7333F11CBF427AA8C4CAFBEB9C57BD2E9973FFD0CB87880CA8E5243A93GG88B9GGD0CB818294G94G88G88GC6FBB0B60CA8E5243A93GG88B9GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG7493GGGG
**end of data**/
}
}