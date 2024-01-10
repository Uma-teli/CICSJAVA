import com.ibm.dbb.build.*
import groovy.util.*

println("Compiling . . .")

def workspace = args[0]
def member = args[1]
def hlq = args[2]
def cpy = args[3]

def memberFile = new CopyToPDS()
memberFile.setDataset("${hlq}.COBOL")
memberFile.setMember("$member")
memberFile.setFile(new File("${workspace}/src/Cobol/${member}.cbl"))
memberFile.execute()

def copyFile = new CopyToPDS()
copyFile.setFile(new File("${workspace}/src/Cobol/copybook/${cpy}.cpy"))
copyFile.setDataset("${hlq}.COPY")
copyFile.setMember("${cpy}")
copyFile.execute()

def compile = new MVSExec().pgm("IGYCRCTL").parm("SIZE(5000K)")
compile.dd(new DDStatement().name("SYSIN").dsn("${hlq}.COBOL($member)").options("shr"))
compile.dd(new DDStatement().name("SYSLIB").dsn("${hlq}.COPY").options("shr"))
compile.dd(new DDStatement().name("SYSLMOD").dsn("${hlq}.load").options("shr"))
compile.dd(new DDStatement().name("SYSLIN").dsn("${hlq}.obj($member)").options("shr").output(true))
compile.dd(new DDStatement().name("SYSUT2").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT1").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT3").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT4").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT5").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT6").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT7").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT8").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT9").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT10").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT11").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT12").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT13").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT14").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT15").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT16").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSUT17").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("SYSMDECK").options("cyl space(5,5) unit(vio) blksize(80) lrecl(80) recfm(f,b) new"))
compile.dd(new DDStatement().name("TASKLIB").dsn("IGY630.SIGYCOMP").options("shr"))
compile.dd(new DDStatement().options("shr").dsn("CEE.SCEERUN"))
compile.dd(new DDStatement().options("shr").dsn("CEE.SCEERUN2"))
compile.dd(new DDStatement().name("SYSPRINT").options("cyl space(5,5) unit(vio) blksize(133) lrecl(133) recfm(f,b) new"))
compile.copy(new CopyToHFS().ddName("SYSPRINT").file(new File("${workspace}/${member}.log")))
def rc = compile.execute()

if (rc > 4) {
    String command = "cat ${workspace}/${member}.log "

    ProcessBuilder builder = new ProcessBuilder("sh", "-c", command)
    builder.redirectErrorStream(true)

    Process process = builder.start()
    process.waitFor()
    if (process.exitValue() == 0) {
    String output = process.getInputStream().getText()
    println "$output"
    }

    throw new Exception("Return code is greater than 4! RC=$rc")
}
else {
    println("Compile successful!  RC=$rc")
}

