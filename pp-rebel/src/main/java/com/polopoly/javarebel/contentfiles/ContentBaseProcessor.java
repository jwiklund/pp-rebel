package com.polopoly.javarebel.contentfiles;

import org.zeroturnaround.bundled.javassist.ClassPool;
import org.zeroturnaround.bundled.javassist.CtClass;
import org.zeroturnaround.bundled.javassist.CtMethod;
import org.zeroturnaround.bundled.javassist.NotFoundException;
import org.zeroturnaround.javarebel.LoggerFactory;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;

public class ContentBaseProcessor extends JavassistClassBytecodeProcessor {
    public void process(ClassPool cp, ClassLoader cl, CtClass ctClass) throws Exception
    {
        try {
            CtMethod externalidMethod = new CtMethod(cp.get("java.lang.String"), "generated$getExternalId", new CtClass[]{}, ctClass);
            externalidMethod.setBody(
                        "{" +
                        "  String externalid = null;" +
                        "  if ($0 instanceof com.polopoly.cm.client.impl.service2client.ContentImpl) {" +
                        "    com.polopoly.cm.client.impl.service2client.ContentImpl impl = (com.polopoly.cm.client.impl.service2client.ContentImpl) $0;" +
                        "    externalid = impl.getExternalId() == null ? null : impl.getExternalId().getExternalId();" +
                        "  }" +
                        "  return externalid;" +
                        "}");
            ctClass.addMethod(externalidMethod);
            CtMethod exportMethod = ctClass.getDeclaredMethod("exportFile");
            exportMethod.insertBefore(
                        "{" +
                        "  if (com.polopoly.javarebel.contentbase.ContentBaseExportHandler.exportFileHandled($0.generated$getExternalId(), $1, $2)) {" +
                        "    return;" +
                        "  }" +
                        "}");
            CtMethod infoMethod = ctClass.getDeclaredMethod("getFileInfo");
            infoMethod.insertBefore(
                        "{" +
                        "  Object[] handled = com.polopoly.javarebel.contentbase.ContentBaseExportHandler.fileInfoHandler($0.generated$getExternalId(), $1);" +
                        "  if (handled != null) {" +
                        "     String dir = (String) handled[0];" +
                        "     String name = (String) handled[1];" +
                        "     boolean directory = ((Boolean) handled[2]).booleanValue();" +
                        "     long lastModified = ((Long) handled[3]).longValue();" +
                        "     long size = ((Long) handled[4]).longValue(); " +
                        "     return new com.polopoly.cm.ContentFileInfo(dir, name, directory, lastModified, size);" +
                        "  }" +
                        "}");
            CtMethod infosMethod = ctClass.getDeclaredMethod("getFileInfos");
            infosMethod.setName("original$getFileInfos");
            CtMethod fixedInfosMethod = new CtMethod(infosMethod.getReturnType(), "getFileInfos", infosMethod.getParameterTypes(), ctClass);
            fixedInfosMethod.setBody(
                        "{" +
                        "  Object[] handled = com.polopoly.javarebel.contentbase.ContentBaseExportHandler.fileInfosHandler($0.generated$getExternalId(), $1);" +
                        "  if (handled == null) {" +
                        "    return $0.original$getFileInfos($1);" +
                        "  } else {" +
                        "    int[] order = (int[]) handled[0];" +
                        "    Object[] done = (Object[]) handled[1];" +
                        "    String[] left = (String[]) handled[2];" +
                        "    com.polopoly.cm.ContentFileInfo[] doneleft;" +
                        "    if (left != null && left.length > 0) {" +
                        "      doneleft = $0.original$getFileInfos(left);" +
                        "    } else {" +
                        "      doneleft = new com.polopoly.cm.ContentFileInfo[0];" +
                        "    }" +
                        "    com.polopoly.cm.ContentFileInfo[] result = new com.polopoly.cm.ContentFileInfo[$1.length];" +
                        "    for (int i = 0 ; i < $1.length ; i++) {" +
                        "      if (order[i] >= 0) {" +
                        "        Object[] packed = (Object[]) done[order[i]];" +
                        "        String dir = (String) packed[0];" +
                        "        String name = (String) packed[1];" +
                        "        boolean directory = ((Boolean) packed[2]).booleanValue();" +
                        "        long lastModified = ((Long) packed[3]).longValue();" +
                        "        long size = ((Long) packed[4]).longValue(); " +
                        "        result[i] = new com.polopoly.cm.ContentFileInfo(dir, name, directory, lastModified, size);" +
                        "      } else {" +
                        "        result[i] = doneleft[-order[i] - 1];" +
                        "      }" +
                        "    }" +
                        "    return result;" +
                        "  }" +
                        "}");
            ctClass.addMethod(fixedInfosMethod);
            LoggerFactory.getInstance().echo("pp-rebel: patched ContentBase to load from FS");
        } catch (NotFoundException e) {
            LoggerFactory.getInstance().error(e);
        }
    }
}
