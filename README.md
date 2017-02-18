# Forge-Script-Compiler
A modification for Minecraft made using Forge, which enables users to compile Java scripts at runtime using methods in a ScriptBase.

Used as a base for a university project and a proof-of-concept for interfacing with Minecraft through scripts compiled at runtime. Should not be used in production, as the script basically allows execution of malicious code.

Scripts are stored in %HOMEPATH%/Scripts, extend ScriptBase and must implement the run method.
