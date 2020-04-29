package com.google.protobuf;

import android.support.v4.os.EnvironmentCompat;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.FieldSet;
import com.google.protobuf.Internal;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.TextFormat;
import com.google.protobuf.WireFormat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Logger;
import kotlin.text.Typography;

public final class Descriptors {
    /* access modifiers changed from: private */
    public static final Logger logger = Logger.getLogger(Descriptors.class.getName());

    public static abstract class GenericDescriptor {
        public abstract FileDescriptor getFile();

        public abstract String getFullName();

        public abstract String getName();

        public abstract Message toProto();
    }

    public static final class FileDescriptor extends GenericDescriptor {
        private final FileDescriptor[] dependencies;
        private final EnumDescriptor[] enumTypes;
        private final FieldDescriptor[] extensions;
        private final Descriptor[] messageTypes;
        /* access modifiers changed from: private */
        public final DescriptorPool pool;
        private DescriptorProtos.FileDescriptorProto proto;
        private final FileDescriptor[] publicDependencies;
        private final ServiceDescriptor[] services;

        @Deprecated
        public interface InternalDescriptorAssigner {
            ExtensionRegistry assignDescriptors(FileDescriptor fileDescriptor);
        }

        public DescriptorProtos.FileDescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public FileDescriptor getFile() {
            return this;
        }

        public String getFullName() {
            return this.proto.getName();
        }

        public String getPackage() {
            return this.proto.getPackage();
        }

        public DescriptorProtos.FileOptions getOptions() {
            return this.proto.getOptions();
        }

        public List<Descriptor> getMessageTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.messageTypes));
        }

        public List<EnumDescriptor> getEnumTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.enumTypes));
        }

        public List<ServiceDescriptor> getServices() {
            return Collections.unmodifiableList(Arrays.asList(this.services));
        }

        public List<FieldDescriptor> getExtensions() {
            return Collections.unmodifiableList(Arrays.asList(this.extensions));
        }

        public List<FileDescriptor> getDependencies() {
            return Collections.unmodifiableList(Arrays.asList(this.dependencies));
        }

        public List<FileDescriptor> getPublicDependencies() {
            return Collections.unmodifiableList(Arrays.asList(this.publicDependencies));
        }

        public enum Syntax {
            UNKNOWN(EnvironmentCompat.MEDIA_UNKNOWN),
            PROTO2("proto2"),
            PROTO3("proto3");
            
            /* access modifiers changed from: private */
            public final String name;

            private Syntax(String name2) {
                this.name = name2;
            }
        }

        public Syntax getSyntax() {
            if (Syntax.PROTO3.name.equals(this.proto.getSyntax())) {
                return Syntax.PROTO3;
            }
            return Syntax.PROTO2;
        }

        public Descriptor findMessageTypeByName(String name) {
            if (name.indexOf(46) != -1) {
                return null;
            }
            String packageName = getPackage();
            if (!packageName.isEmpty()) {
                name = packageName + '.' + name;
            }
            GenericDescriptor result = this.pool.findSymbol(name);
            if (result == null || !(result instanceof Descriptor) || result.getFile() != this) {
                return null;
            }
            return (Descriptor) result;
        }

        public EnumDescriptor findEnumTypeByName(String name) {
            if (name.indexOf(46) != -1) {
                return null;
            }
            String packageName = getPackage();
            if (!packageName.isEmpty()) {
                name = packageName + '.' + name;
            }
            GenericDescriptor result = this.pool.findSymbol(name);
            if (result == null || !(result instanceof EnumDescriptor) || result.getFile() != this) {
                return null;
            }
            return (EnumDescriptor) result;
        }

        public ServiceDescriptor findServiceByName(String name) {
            if (name.indexOf(46) != -1) {
                return null;
            }
            String packageName = getPackage();
            if (!packageName.isEmpty()) {
                name = packageName + '.' + name;
            }
            GenericDescriptor result = this.pool.findSymbol(name);
            if (result == null || !(result instanceof ServiceDescriptor) || result.getFile() != this) {
                return null;
            }
            return (ServiceDescriptor) result;
        }

        public FieldDescriptor findExtensionByName(String name) {
            if (name.indexOf(46) != -1) {
                return null;
            }
            String packageName = getPackage();
            if (!packageName.isEmpty()) {
                name = packageName + '.' + name;
            }
            GenericDescriptor result = this.pool.findSymbol(name);
            if (result == null || !(result instanceof FieldDescriptor) || result.getFile() != this) {
                return null;
            }
            return (FieldDescriptor) result;
        }

        public static FileDescriptor buildFrom(DescriptorProtos.FileDescriptorProto proto2, FileDescriptor[] dependencies2) throws DescriptorValidationException {
            return buildFrom(proto2, dependencies2, false);
        }

        public static FileDescriptor buildFrom(DescriptorProtos.FileDescriptorProto proto2, FileDescriptor[] dependencies2, boolean allowUnknownDependencies) throws DescriptorValidationException {
            FileDescriptor result = new FileDescriptor(proto2, dependencies2, new DescriptorPool(dependencies2, allowUnknownDependencies), allowUnknownDependencies);
            result.crossLink();
            return result;
        }

        private static byte[] latin1Cat(String[] strings) {
            if (strings.length == 1) {
                return strings[0].getBytes(Internal.ISO_8859_1);
            }
            StringBuilder descriptorData = new StringBuilder();
            for (String part : strings) {
                descriptorData.append(part);
            }
            return descriptorData.toString().getBytes(Internal.ISO_8859_1);
        }

        private static FileDescriptor[] findDescriptors(Class<?> descriptorOuterClass, String[] dependencyClassNames, String[] dependencyFileNames) {
            List<FileDescriptor> descriptors = new ArrayList<>();
            for (int i = 0; i < dependencyClassNames.length; i++) {
                try {
                    descriptors.add((FileDescriptor) descriptorOuterClass.getClassLoader().loadClass(dependencyClassNames[i]).getField("descriptor").get(null));
                } catch (Exception e) {
                    Descriptors.logger.warning("Descriptors for \"" + dependencyFileNames[i] + "\" can not be found.");
                }
            }
            return (FileDescriptor[]) descriptors.toArray(new FileDescriptor[0]);
        }

        @Deprecated
        public static void internalBuildGeneratedFileFrom(String[] descriptorDataParts, FileDescriptor[] dependencies2, InternalDescriptorAssigner descriptorAssigner) {
            byte[] descriptorBytes = latin1Cat(descriptorDataParts);
            try {
                DescriptorProtos.FileDescriptorProto proto2 = DescriptorProtos.FileDescriptorProto.parseFrom(descriptorBytes);
                try {
                    FileDescriptor result = buildFrom(proto2, dependencies2, true);
                    ExtensionRegistry registry = descriptorAssigner.assignDescriptors(result);
                    if (registry != null) {
                        try {
                            result.setProto(DescriptorProtos.FileDescriptorProto.parseFrom(descriptorBytes, registry));
                        } catch (InvalidProtocolBufferException e) {
                            throw new IllegalArgumentException("Failed to parse protocol buffer descriptor for generated code.", e);
                        }
                    }
                } catch (DescriptorValidationException e2) {
                    throw new IllegalArgumentException("Invalid embedded descriptor for \"" + proto2.getName() + "\".", e2);
                }
            } catch (InvalidProtocolBufferException e3) {
                throw new IllegalArgumentException("Failed to parse protocol buffer descriptor for generated code.", e3);
            }
        }

        public static FileDescriptor internalBuildGeneratedFileFrom(String[] descriptorDataParts, FileDescriptor[] dependencies2) {
            try {
                DescriptorProtos.FileDescriptorProto proto2 = DescriptorProtos.FileDescriptorProto.parseFrom(latin1Cat(descriptorDataParts));
                try {
                    return buildFrom(proto2, dependencies2, true);
                } catch (DescriptorValidationException e) {
                    throw new IllegalArgumentException("Invalid embedded descriptor for \"" + proto2.getName() + "\".", e);
                }
            } catch (InvalidProtocolBufferException e2) {
                throw new IllegalArgumentException("Failed to parse protocol buffer descriptor for generated code.", e2);
            }
        }

        @Deprecated
        public static void internalBuildGeneratedFileFrom(String[] descriptorDataParts, Class<?> descriptorOuterClass, String[] dependencyClassNames, String[] dependencyFileNames, InternalDescriptorAssigner descriptorAssigner) {
            internalBuildGeneratedFileFrom(descriptorDataParts, findDescriptors(descriptorOuterClass, dependencyClassNames, dependencyFileNames), descriptorAssigner);
        }

        public static FileDescriptor internalBuildGeneratedFileFrom(String[] descriptorDataParts, Class<?> descriptorOuterClass, String[] dependencyClassNames, String[] dependencyFileNames) {
            return internalBuildGeneratedFileFrom(descriptorDataParts, findDescriptors(descriptorOuterClass, dependencyClassNames, dependencyFileNames));
        }

        public static void internalUpdateFileDescriptor(FileDescriptor descriptor, ExtensionRegistry registry) {
            try {
                descriptor.setProto(DescriptorProtos.FileDescriptorProto.parseFrom(descriptor.proto.toByteString(), registry));
            } catch (InvalidProtocolBufferException e) {
                throw new IllegalArgumentException("Failed to parse protocol buffer descriptor for generated code.", e);
            }
        }

        private FileDescriptor(DescriptorProtos.FileDescriptorProto proto2, FileDescriptor[] dependencies2, DescriptorPool pool2, boolean allowUnknownDependencies) throws DescriptorValidationException {
            this.pool = pool2;
            this.proto = proto2;
            this.dependencies = (FileDescriptor[]) dependencies2.clone();
            HashMap<String, FileDescriptor> nameToFileMap = new HashMap<>();
            for (FileDescriptor file : dependencies2) {
                nameToFileMap.put(file.getName(), file);
            }
            List<FileDescriptor> publicDependencies2 = new ArrayList<>();
            for (int i = 0; i < proto2.getPublicDependencyCount(); i++) {
                int index = proto2.getPublicDependency(i);
                if (index < 0 || index >= proto2.getDependencyCount()) {
                    throw new DescriptorValidationException(this, "Invalid public dependency index.");
                }
                String name = proto2.getDependency(index);
                FileDescriptor file2 = (FileDescriptor) nameToFileMap.get(name);
                if (file2 != null) {
                    publicDependencies2.add(file2);
                } else if (!allowUnknownDependencies) {
                    throw new DescriptorValidationException(this, "Invalid public dependency: " + name);
                }
            }
            this.publicDependencies = new FileDescriptor[publicDependencies2.size()];
            publicDependencies2.toArray(this.publicDependencies);
            pool2.addPackage(getPackage(), this);
            this.messageTypes = new Descriptor[proto2.getMessageTypeCount()];
            for (int i2 = 0; i2 < proto2.getMessageTypeCount(); i2++) {
                this.messageTypes[i2] = new Descriptor(proto2.getMessageType(i2), this, null, i2);
            }
            this.enumTypes = new EnumDescriptor[proto2.getEnumTypeCount()];
            for (int i3 = 0; i3 < proto2.getEnumTypeCount(); i3++) {
                this.enumTypes[i3] = new EnumDescriptor(proto2.getEnumType(i3), this, null, i3);
            }
            this.services = new ServiceDescriptor[proto2.getServiceCount()];
            for (int i4 = 0; i4 < proto2.getServiceCount(); i4++) {
                this.services[i4] = new ServiceDescriptor(proto2.getService(i4), this, i4);
            }
            this.extensions = new FieldDescriptor[proto2.getExtensionCount()];
            for (int i5 = 0; i5 < proto2.getExtensionCount(); i5++) {
                this.extensions[i5] = new FieldDescriptor(proto2.getExtension(i5), this, null, i5, true);
            }
        }

        FileDescriptor(String packageName, Descriptor message) throws DescriptorValidationException {
            this.pool = new DescriptorPool(new FileDescriptor[0], true);
            this.proto = DescriptorProtos.FileDescriptorProto.newBuilder().setName(message.getFullName() + ".placeholder.proto").setPackage(packageName).addMessageType(message.toProto()).build();
            this.dependencies = new FileDescriptor[0];
            this.publicDependencies = new FileDescriptor[0];
            this.messageTypes = new Descriptor[]{message};
            this.enumTypes = new EnumDescriptor[0];
            this.services = new ServiceDescriptor[0];
            this.extensions = new FieldDescriptor[0];
            this.pool.addPackage(packageName, this);
            this.pool.addSymbol(message);
        }

        private void crossLink() throws DescriptorValidationException {
            for (Descriptor messageType : this.messageTypes) {
                messageType.crossLink();
            }
            for (ServiceDescriptor service : this.services) {
                service.crossLink();
            }
            for (FieldDescriptor extension : this.extensions) {
                extension.crossLink();
            }
        }

        private void setProto(DescriptorProtos.FileDescriptorProto proto2) {
            this.proto = proto2;
            for (int i = 0; i < this.messageTypes.length; i++) {
                this.messageTypes[i].setProto(proto2.getMessageType(i));
            }
            for (int i2 = 0; i2 < this.enumTypes.length; i2++) {
                this.enumTypes[i2].setProto(proto2.getEnumType(i2));
            }
            for (int i3 = 0; i3 < this.services.length; i3++) {
                this.services[i3].setProto(proto2.getService(i3));
            }
            for (int i4 = 0; i4 < this.extensions.length; i4++) {
                this.extensions[i4].setProto(proto2.getExtension(i4));
            }
        }

        /* access modifiers changed from: package-private */
        public boolean supportsUnknownEnumValue() {
            return getSyntax() == Syntax.PROTO3;
        }
    }

    public static final class Descriptor extends GenericDescriptor {
        private final Descriptor containingType;
        private final EnumDescriptor[] enumTypes;
        private final FieldDescriptor[] extensions;
        private final FieldDescriptor[] fields;
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private final Descriptor[] nestedTypes;
        private final OneofDescriptor[] oneofs;
        private DescriptorProtos.DescriptorProto proto;

        public int getIndex() {
            return this.index;
        }

        public DescriptorProtos.DescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public String getFullName() {
            return this.fullName;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public Descriptor getContainingType() {
            return this.containingType;
        }

        public DescriptorProtos.MessageOptions getOptions() {
            return this.proto.getOptions();
        }

        public List<FieldDescriptor> getFields() {
            return Collections.unmodifiableList(Arrays.asList(this.fields));
        }

        public List<OneofDescriptor> getOneofs() {
            return Collections.unmodifiableList(Arrays.asList(this.oneofs));
        }

        public List<FieldDescriptor> getExtensions() {
            return Collections.unmodifiableList(Arrays.asList(this.extensions));
        }

        public List<Descriptor> getNestedTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.nestedTypes));
        }

        public List<EnumDescriptor> getEnumTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.enumTypes));
        }

        public boolean isExtensionNumber(int number) {
            for (DescriptorProtos.DescriptorProto.ExtensionRange range : this.proto.getExtensionRangeList()) {
                if (range.getStart() <= number && number < range.getEnd()) {
                    return true;
                }
            }
            return false;
        }

        public boolean isReservedNumber(int number) {
            for (DescriptorProtos.DescriptorProto.ReservedRange range : this.proto.getReservedRangeList()) {
                if (range.getStart() <= number && number < range.getEnd()) {
                    return true;
                }
            }
            return false;
        }

        public boolean isReservedName(String name) {
            Internal.checkNotNull(name);
            for (String reservedName : this.proto.getReservedNameList()) {
                if (reservedName.equals(name)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isExtendable() {
            return this.proto.getExtensionRangeList().size() != 0;
        }

        public FieldDescriptor findFieldByName(String name) {
            GenericDescriptor result = this.file.pool.findSymbol(this.fullName + '.' + name);
            if (result == null || !(result instanceof FieldDescriptor)) {
                return null;
            }
            return (FieldDescriptor) result;
        }

        public FieldDescriptor findFieldByNumber(int number) {
            return (FieldDescriptor) this.file.pool.fieldsByNumber.get(new DescriptorPool.DescriptorIntPair(this, number));
        }

        public Descriptor findNestedTypeByName(String name) {
            GenericDescriptor result = this.file.pool.findSymbol(this.fullName + '.' + name);
            if (result == null || !(result instanceof Descriptor)) {
                return null;
            }
            return (Descriptor) result;
        }

        public EnumDescriptor findEnumTypeByName(String name) {
            GenericDescriptor result = this.file.pool.findSymbol(this.fullName + '.' + name);
            if (result == null || !(result instanceof EnumDescriptor)) {
                return null;
            }
            return (EnumDescriptor) result;
        }

        Descriptor(String fullname) throws DescriptorValidationException {
            String name = fullname;
            String packageName = "";
            int pos = fullname.lastIndexOf(46);
            if (pos != -1) {
                name = fullname.substring(pos + 1);
                packageName = fullname.substring(0, pos);
            }
            this.index = 0;
            this.proto = DescriptorProtos.DescriptorProto.newBuilder().setName(name).addExtensionRange(DescriptorProtos.DescriptorProto.ExtensionRange.newBuilder().setStart(1).setEnd(536870912).build()).build();
            this.fullName = fullname;
            this.containingType = null;
            this.nestedTypes = new Descriptor[0];
            this.enumTypes = new EnumDescriptor[0];
            this.fields = new FieldDescriptor[0];
            this.extensions = new FieldDescriptor[0];
            this.oneofs = new OneofDescriptor[0];
            this.file = new FileDescriptor(packageName, this);
        }

        private Descriptor(DescriptorProtos.DescriptorProto proto2, FileDescriptor file2, Descriptor parent, int index2) throws DescriptorValidationException {
            this.index = index2;
            this.proto = proto2;
            this.fullName = Descriptors.computeFullName(file2, parent, proto2.getName());
            this.file = file2;
            this.containingType = parent;
            this.oneofs = new OneofDescriptor[proto2.getOneofDeclCount()];
            for (int i = 0; i < proto2.getOneofDeclCount(); i++) {
                this.oneofs[i] = new OneofDescriptor(proto2.getOneofDecl(i), file2, this, i);
            }
            this.nestedTypes = new Descriptor[proto2.getNestedTypeCount()];
            for (int i2 = 0; i2 < proto2.getNestedTypeCount(); i2++) {
                this.nestedTypes[i2] = new Descriptor(proto2.getNestedType(i2), file2, this, i2);
            }
            this.enumTypes = new EnumDescriptor[proto2.getEnumTypeCount()];
            for (int i3 = 0; i3 < proto2.getEnumTypeCount(); i3++) {
                this.enumTypes[i3] = new EnumDescriptor(proto2.getEnumType(i3), file2, this, i3);
            }
            this.fields = new FieldDescriptor[proto2.getFieldCount()];
            for (int i4 = 0; i4 < proto2.getFieldCount(); i4++) {
                this.fields[i4] = new FieldDescriptor(proto2.getField(i4), file2, this, i4, false);
            }
            this.extensions = new FieldDescriptor[proto2.getExtensionCount()];
            for (int i5 = 0; i5 < proto2.getExtensionCount(); i5++) {
                this.extensions[i5] = new FieldDescriptor(proto2.getExtension(i5), file2, this, i5, true);
            }
            for (int i6 = 0; i6 < proto2.getOneofDeclCount(); i6++) {
                FieldDescriptor[] unused = this.oneofs[i6].fields = new FieldDescriptor[this.oneofs[i6].getFieldCount()];
                int unused2 = this.oneofs[i6].fieldCount = 0;
            }
            for (int i7 = 0; i7 < proto2.getFieldCount(); i7++) {
                OneofDescriptor oneofDescriptor = this.fields[i7].getContainingOneof();
                if (oneofDescriptor != null) {
                    oneofDescriptor.fields[OneofDescriptor.access$1908(oneofDescriptor)] = this.fields[i7];
                }
            }
            file2.pool.addSymbol(this);
        }

        /* access modifiers changed from: private */
        public void crossLink() throws DescriptorValidationException {
            for (Descriptor nestedType : this.nestedTypes) {
                nestedType.crossLink();
            }
            for (FieldDescriptor field : this.fields) {
                field.crossLink();
            }
            for (FieldDescriptor extension : this.extensions) {
                extension.crossLink();
            }
        }

        /* access modifiers changed from: private */
        public void setProto(DescriptorProtos.DescriptorProto proto2) {
            this.proto = proto2;
            for (int i = 0; i < this.nestedTypes.length; i++) {
                this.nestedTypes[i].setProto(proto2.getNestedType(i));
            }
            for (int i2 = 0; i2 < this.oneofs.length; i2++) {
                this.oneofs[i2].setProto(proto2.getOneofDecl(i2));
            }
            for (int i3 = 0; i3 < this.enumTypes.length; i3++) {
                this.enumTypes[i3].setProto(proto2.getEnumType(i3));
            }
            for (int i4 = 0; i4 < this.fields.length; i4++) {
                this.fields[i4].setProto(proto2.getField(i4));
            }
            for (int i5 = 0; i5 < this.extensions.length; i5++) {
                this.extensions[i5].setProto(proto2.getExtension(i5));
            }
        }
    }

    public static final class FieldDescriptor extends GenericDescriptor implements Comparable<FieldDescriptor>, FieldSet.FieldDescriptorLite<FieldDescriptor> {
        private static final WireFormat.FieldType[] table = WireFormat.FieldType.values();
        private OneofDescriptor containingOneof;
        private Descriptor containingType;
        private Object defaultValue;
        private EnumDescriptor enumType;
        private final Descriptor extensionScope;
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private final String jsonName;
        private Descriptor messageType;
        private DescriptorProtos.FieldDescriptorProto proto;
        private Type type;

        public int getIndex() {
            return this.index;
        }

        public DescriptorProtos.FieldDescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public int getNumber() {
            return this.proto.getNumber();
        }

        public String getFullName() {
            return this.fullName;
        }

        public String getJsonName() {
            return this.jsonName;
        }

        public JavaType getJavaType() {
            return this.type.getJavaType();
        }

        public WireFormat.JavaType getLiteJavaType() {
            return getLiteType().getJavaType();
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public Type getType() {
            return this.type;
        }

        public WireFormat.FieldType getLiteType() {
            return table[this.type.ordinal()];
        }

        public boolean needsUtf8Check() {
            if (this.type != Type.STRING) {
                return false;
            }
            if (getContainingType().getOptions().getMapEntry() || getFile().getSyntax() == FileDescriptor.Syntax.PROTO3) {
                return true;
            }
            return getFile().getOptions().getJavaStringCheckUtf8();
        }

        public boolean isMapField() {
            return getType() == Type.MESSAGE && isRepeated() && getMessageType().getOptions().getMapEntry();
        }

        static {
            if (Type.values().length != DescriptorProtos.FieldDescriptorProto.Type.values().length) {
                throw new RuntimeException("descriptor.proto has a new declared type but Descriptors.java wasn't updated.");
            }
        }

        public boolean isRequired() {
            return this.proto.getLabel() == DescriptorProtos.FieldDescriptorProto.Label.LABEL_REQUIRED;
        }

        public boolean isOptional() {
            return this.proto.getLabel() == DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL;
        }

        public boolean isRepeated() {
            return this.proto.getLabel() == DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED;
        }

        public boolean isPacked() {
            if (!isPackable()) {
                return false;
            }
            if (getFile().getSyntax() == FileDescriptor.Syntax.PROTO2) {
                return getOptions().getPacked();
            }
            if (!getOptions().hasPacked() || getOptions().getPacked()) {
                return true;
            }
            return false;
        }

        public boolean isPackable() {
            return isRepeated() && getLiteType().isPackable();
        }

        public boolean hasDefaultValue() {
            return this.proto.hasDefaultValue();
        }

        public Object getDefaultValue() {
            if (getJavaType() != JavaType.MESSAGE) {
                return this.defaultValue;
            }
            throw new UnsupportedOperationException("FieldDescriptor.getDefaultValue() called on an embedded message field.");
        }

        public DescriptorProtos.FieldOptions getOptions() {
            return this.proto.getOptions();
        }

        public boolean isExtension() {
            return this.proto.hasExtendee();
        }

        public Descriptor getContainingType() {
            return this.containingType;
        }

        public OneofDescriptor getContainingOneof() {
            return this.containingOneof;
        }

        public Descriptor getExtensionScope() {
            if (isExtension()) {
                return this.extensionScope;
            }
            throw new UnsupportedOperationException(String.format("This field is not an extension. (%s)", this.fullName));
        }

        public Descriptor getMessageType() {
            if (getJavaType() == JavaType.MESSAGE) {
                return this.messageType;
            }
            throw new UnsupportedOperationException(String.format("This field is not of message type. (%s)", this.fullName));
        }

        public EnumDescriptor getEnumType() {
            if (getJavaType() == JavaType.ENUM) {
                return this.enumType;
            }
            throw new UnsupportedOperationException(String.format("This field is not of enum type. (%s)", this.fullName));
        }

        public int compareTo(FieldDescriptor other) {
            if (other.containingType == this.containingType) {
                return getNumber() - other.getNumber();
            }
            throw new IllegalArgumentException("FieldDescriptors can only be compared to other FieldDescriptors for fields of the same message type.");
        }

        public String toString() {
            return getFullName();
        }

        public enum Type {
            DOUBLE(JavaType.DOUBLE),
            FLOAT(JavaType.FLOAT),
            INT64(JavaType.LONG),
            UINT64(JavaType.LONG),
            INT32(JavaType.INT),
            FIXED64(JavaType.LONG),
            FIXED32(JavaType.INT),
            BOOL(JavaType.BOOLEAN),
            STRING(JavaType.STRING),
            GROUP(JavaType.MESSAGE),
            MESSAGE(JavaType.MESSAGE),
            BYTES(JavaType.BYTE_STRING),
            UINT32(JavaType.INT),
            ENUM(JavaType.ENUM),
            SFIXED32(JavaType.INT),
            SFIXED64(JavaType.LONG),
            SINT32(JavaType.INT),
            SINT64(JavaType.LONG);
            
            private JavaType javaType;

            private Type(JavaType javaType2) {
                this.javaType = javaType2;
            }

            public DescriptorProtos.FieldDescriptorProto.Type toProto() {
                return DescriptorProtos.FieldDescriptorProto.Type.forNumber(ordinal() + 1);
            }

            public JavaType getJavaType() {
                return this.javaType;
            }

            public static Type valueOf(DescriptorProtos.FieldDescriptorProto.Type type) {
                return values()[type.getNumber() - 1];
            }
        }

        public enum JavaType {
            INT(0),
            LONG(0L),
            FLOAT(Float.valueOf(0.0f)),
            DOUBLE(Double.valueOf(0.0d)),
            BOOLEAN(false),
            STRING(""),
            BYTE_STRING(ByteString.EMPTY),
            ENUM(null),
            MESSAGE(null);
            
            /* access modifiers changed from: private */
            public final Object defaultDefault;

            private JavaType(Object defaultDefault2) {
                this.defaultDefault = defaultDefault2;
            }
        }

        private static String fieldNameToJsonName(String name) {
            int length = name.length();
            StringBuilder result = new StringBuilder(length);
            boolean isNextUpperCase = false;
            for (int i = 0; i < length; i++) {
                char ch = name.charAt(i);
                if (ch == '_') {
                    isNextUpperCase = true;
                } else if (isNextUpperCase) {
                    if ('a' <= ch && ch <= 'z') {
                        ch = (char) ((ch - 'a') + 65);
                    }
                    result.append(ch);
                    isNextUpperCase = false;
                } else {
                    result.append(ch);
                }
            }
            return result.toString();
        }

        private FieldDescriptor(DescriptorProtos.FieldDescriptorProto proto2, FileDescriptor file2, Descriptor parent, int index2, boolean isExtension) throws DescriptorValidationException {
            this.index = index2;
            this.proto = proto2;
            this.fullName = Descriptors.computeFullName(file2, parent, proto2.getName());
            this.file = file2;
            if (proto2.hasJsonName()) {
                this.jsonName = proto2.getJsonName();
            } else {
                this.jsonName = fieldNameToJsonName(proto2.getName());
            }
            if (proto2.hasType()) {
                this.type = Type.valueOf(proto2.getType());
            }
            if (getNumber() <= 0) {
                throw new DescriptorValidationException(this, "Field numbers must be positive integers.");
            }
            if (isExtension) {
                if (!proto2.hasExtendee()) {
                    throw new DescriptorValidationException(this, "FieldDescriptorProto.extendee not set for extension field.");
                }
                this.containingType = null;
                if (parent != null) {
                    this.extensionScope = parent;
                } else {
                    this.extensionScope = null;
                }
                if (proto2.hasOneofIndex()) {
                    throw new DescriptorValidationException(this, "FieldDescriptorProto.oneof_index set for extension field.");
                }
                this.containingOneof = null;
            } else if (proto2.hasExtendee()) {
                throw new DescriptorValidationException(this, "FieldDescriptorProto.extendee set for non-extension field.");
            } else {
                this.containingType = parent;
                if (!proto2.hasOneofIndex()) {
                    this.containingOneof = null;
                } else if (proto2.getOneofIndex() < 0 || proto2.getOneofIndex() >= parent.toProto().getOneofDeclCount()) {
                    throw new DescriptorValidationException(this, "FieldDescriptorProto.oneof_index is out of range for type " + parent.getName());
                } else {
                    this.containingOneof = parent.getOneofs().get(proto2.getOneofIndex());
                    OneofDescriptor.access$1908(this.containingOneof);
                }
                this.extensionScope = null;
            }
            file2.pool.addSymbol(this);
        }

        /* access modifiers changed from: private */
        public void crossLink() throws DescriptorValidationException {
            if (this.proto.hasExtendee()) {
                GenericDescriptor extendee = this.file.pool.lookupSymbol(this.proto.getExtendee(), this, DescriptorPool.SearchFilter.TYPES_ONLY);
                if (!(extendee instanceof Descriptor)) {
                    throw new DescriptorValidationException(this, ((char) Typography.quote) + this.proto.getExtendee() + "\" is not a message type.");
                }
                this.containingType = (Descriptor) extendee;
                if (!getContainingType().isExtensionNumber(getNumber())) {
                    throw new DescriptorValidationException(this, ((char) Typography.quote) + getContainingType().getFullName() + "\" does not declare " + getNumber() + " as an extension number.");
                }
            }
            if (this.proto.hasTypeName()) {
                GenericDescriptor typeDescriptor = this.file.pool.lookupSymbol(this.proto.getTypeName(), this, DescriptorPool.SearchFilter.TYPES_ONLY);
                if (!this.proto.hasType()) {
                    if (typeDescriptor instanceof Descriptor) {
                        this.type = Type.MESSAGE;
                    } else if (typeDescriptor instanceof EnumDescriptor) {
                        this.type = Type.ENUM;
                    } else {
                        throw new DescriptorValidationException(this, ((char) Typography.quote) + this.proto.getTypeName() + "\" is not a type.");
                    }
                }
                if (getJavaType() == JavaType.MESSAGE) {
                    if (!(typeDescriptor instanceof Descriptor)) {
                        throw new DescriptorValidationException(this, ((char) Typography.quote) + this.proto.getTypeName() + "\" is not a message type.");
                    }
                    this.messageType = (Descriptor) typeDescriptor;
                    if (this.proto.hasDefaultValue()) {
                        throw new DescriptorValidationException(this, "Messages can't have default values.");
                    }
                } else if (getJavaType() != JavaType.ENUM) {
                    throw new DescriptorValidationException(this, "Field with primitive type has type_name.");
                } else if (!(typeDescriptor instanceof EnumDescriptor)) {
                    throw new DescriptorValidationException(this, ((char) Typography.quote) + this.proto.getTypeName() + "\" is not an enum type.");
                } else {
                    this.enumType = (EnumDescriptor) typeDescriptor;
                }
            } else if (getJavaType() == JavaType.MESSAGE || getJavaType() == JavaType.ENUM) {
                throw new DescriptorValidationException(this, "Field with message or enum type missing type_name.");
            }
            if (!this.proto.getOptions().getPacked() || isPackable()) {
                if (!this.proto.hasDefaultValue()) {
                    if (!isRepeated()) {
                        switch (getJavaType()) {
                            case ENUM:
                                this.defaultValue = this.enumType.getValues().get(0);
                                break;
                            case MESSAGE:
                                this.defaultValue = null;
                                break;
                            default:
                                this.defaultValue = getJavaType().defaultDefault;
                                break;
                        }
                    } else {
                        this.defaultValue = Collections.emptyList();
                    }
                } else if (isRepeated()) {
                    throw new DescriptorValidationException(this, "Repeated fields cannot have default values.");
                } else {
                    try {
                        switch (getType()) {
                            case INT32:
                            case SINT32:
                            case SFIXED32:
                                this.defaultValue = Integer.valueOf(TextFormat.parseInt32(this.proto.getDefaultValue()));
                                break;
                            case UINT32:
                            case FIXED32:
                                this.defaultValue = Integer.valueOf(TextFormat.parseUInt32(this.proto.getDefaultValue()));
                                break;
                            case INT64:
                            case SINT64:
                            case SFIXED64:
                                this.defaultValue = Long.valueOf(TextFormat.parseInt64(this.proto.getDefaultValue()));
                                break;
                            case UINT64:
                            case FIXED64:
                                this.defaultValue = Long.valueOf(TextFormat.parseUInt64(this.proto.getDefaultValue()));
                                break;
                            case FLOAT:
                                if (!this.proto.getDefaultValue().equals("inf")) {
                                    if (!this.proto.getDefaultValue().equals("-inf")) {
                                        if (!this.proto.getDefaultValue().equals("nan")) {
                                            this.defaultValue = Float.valueOf(this.proto.getDefaultValue());
                                            break;
                                        } else {
                                            this.defaultValue = Float.valueOf(Float.NaN);
                                            break;
                                        }
                                    } else {
                                        this.defaultValue = Float.valueOf(Float.NEGATIVE_INFINITY);
                                        break;
                                    }
                                } else {
                                    this.defaultValue = Float.valueOf(Float.POSITIVE_INFINITY);
                                    break;
                                }
                            case DOUBLE:
                                if (!this.proto.getDefaultValue().equals("inf")) {
                                    if (!this.proto.getDefaultValue().equals("-inf")) {
                                        if (!this.proto.getDefaultValue().equals("nan")) {
                                            this.defaultValue = Double.valueOf(this.proto.getDefaultValue());
                                            break;
                                        } else {
                                            this.defaultValue = Double.valueOf(Double.NaN);
                                            break;
                                        }
                                    } else {
                                        this.defaultValue = Double.valueOf(Double.NEGATIVE_INFINITY);
                                        break;
                                    }
                                } else {
                                    this.defaultValue = Double.valueOf(Double.POSITIVE_INFINITY);
                                    break;
                                }
                            case BOOL:
                                this.defaultValue = Boolean.valueOf(this.proto.getDefaultValue());
                                break;
                            case STRING:
                                this.defaultValue = this.proto.getDefaultValue();
                                break;
                            case BYTES:
                                this.defaultValue = TextFormat.unescapeBytes(this.proto.getDefaultValue());
                                break;
                            case ENUM:
                                this.defaultValue = this.enumType.findValueByName(this.proto.getDefaultValue());
                                if (this.defaultValue == null) {
                                    throw new DescriptorValidationException(this, "Unknown enum default value: \"" + this.proto.getDefaultValue() + ((char) Typography.quote));
                                }
                                break;
                            case MESSAGE:
                            case GROUP:
                                throw new DescriptorValidationException(this, "Message type had default value.");
                        }
                    } catch (TextFormat.InvalidEscapeSequenceException e) {
                        throw new DescriptorValidationException(this, "Couldn't parse default value: " + e.getMessage(), e);
                    } catch (NumberFormatException e2) {
                        throw new DescriptorValidationException(this, "Could not parse default value: \"" + this.proto.getDefaultValue() + ((char) Typography.quote), e2);
                    }
                }
                if (!isExtension()) {
                    this.file.pool.addFieldByNumber(this);
                }
                if (this.containingType != null && this.containingType.getOptions().getMessageSetWireFormat()) {
                    if (!isExtension()) {
                        throw new DescriptorValidationException(this, "MessageSets cannot have fields, only extensions.");
                    } else if (!isOptional() || getType() != Type.MESSAGE) {
                        throw new DescriptorValidationException(this, "Extensions of MessageSets must be optional messages.");
                    }
                }
            } else {
                throw new DescriptorValidationException(this, "[packed = true] can only be specified for repeated primitive fields.");
            }
        }

        /* access modifiers changed from: private */
        public void setProto(DescriptorProtos.FieldDescriptorProto proto2) {
            this.proto = proto2;
        }

        public MessageLite.Builder internalMergeFrom(MessageLite.Builder to, MessageLite from) {
            return ((Message.Builder) to).mergeFrom((Message) from);
        }
    }

    public static final class EnumDescriptor extends GenericDescriptor implements Internal.EnumLiteMap<EnumValueDescriptor> {
        private final Descriptor containingType;
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private DescriptorProtos.EnumDescriptorProto proto;
        private final WeakHashMap<Integer, WeakReference<EnumValueDescriptor>> unknownValues;
        private EnumValueDescriptor[] values;

        public int getIndex() {
            return this.index;
        }

        public DescriptorProtos.EnumDescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public String getFullName() {
            return this.fullName;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public Descriptor getContainingType() {
            return this.containingType;
        }

        public DescriptorProtos.EnumOptions getOptions() {
            return this.proto.getOptions();
        }

        public List<EnumValueDescriptor> getValues() {
            return Collections.unmodifiableList(Arrays.asList(this.values));
        }

        public EnumValueDescriptor findValueByName(String name) {
            GenericDescriptor result = this.file.pool.findSymbol(this.fullName + '.' + name);
            if (result == null || !(result instanceof EnumValueDescriptor)) {
                return null;
            }
            return (EnumValueDescriptor) result;
        }

        public EnumValueDescriptor findValueByNumber(int number) {
            return (EnumValueDescriptor) this.file.pool.enumValuesByNumber.get(new DescriptorPool.DescriptorIntPair(this, number));
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v6, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: com.google.protobuf.Descriptors$EnumValueDescriptor} */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0037, code lost:
            r4 = r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
            return r3;
         */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.google.protobuf.Descriptors.EnumValueDescriptor findValueByNumberCreatingIfUnknown(int r9) {
            /*
                r8 = this;
                com.google.protobuf.Descriptors$EnumValueDescriptor r3 = r8.findValueByNumber(r9)
                if (r3 == 0) goto L_0x0009
                r4 = r3
                r5 = r3
            L_0x0008:
                return r5
            L_0x0009:
                monitor-enter(r8)
                java.lang.Integer r1 = new java.lang.Integer     // Catch:{ all -> 0x003a }
                r1.<init>(r9)     // Catch:{ all -> 0x003a }
                java.util.WeakHashMap<java.lang.Integer, java.lang.ref.WeakReference<com.google.protobuf.Descriptors$EnumValueDescriptor>> r6 = r8.unknownValues     // Catch:{ all -> 0x003a }
                java.lang.Object r2 = r6.get(r1)     // Catch:{ all -> 0x003a }
                java.lang.ref.WeakReference r2 = (java.lang.ref.WeakReference) r2     // Catch:{ all -> 0x003a }
                if (r2 == 0) goto L_0x0042
                java.lang.Object r6 = r2.get()     // Catch:{ all -> 0x003a }
                r0 = r6
                com.google.protobuf.Descriptors$EnumValueDescriptor r0 = (com.google.protobuf.Descriptors.EnumValueDescriptor) r0     // Catch:{ all -> 0x003a }
                r3 = r0
                r4 = r3
            L_0x0022:
                if (r4 != 0) goto L_0x0040
                com.google.protobuf.Descriptors$EnumValueDescriptor r3 = new com.google.protobuf.Descriptors$EnumValueDescriptor     // Catch:{ all -> 0x003d }
                com.google.protobuf.Descriptors$FileDescriptor r6 = r8.file     // Catch:{ all -> 0x003d }
                r7 = 0
                r3.<init>(r6, r8, r1)     // Catch:{ all -> 0x003d }
                java.util.WeakHashMap<java.lang.Integer, java.lang.ref.WeakReference<com.google.protobuf.Descriptors$EnumValueDescriptor>> r6 = r8.unknownValues     // Catch:{ all -> 0x003a }
                java.lang.ref.WeakReference r7 = new java.lang.ref.WeakReference     // Catch:{ all -> 0x003a }
                r7.<init>(r3)     // Catch:{ all -> 0x003a }
                r6.put(r1, r7)     // Catch:{ all -> 0x003a }
            L_0x0036:
                monitor-exit(r8)     // Catch:{ all -> 0x003a }
                r4 = r3
                r5 = r3
                goto L_0x0008
            L_0x003a:
                r6 = move-exception
            L_0x003b:
                monitor-exit(r8)     // Catch:{ all -> 0x003a }
                throw r6
            L_0x003d:
                r6 = move-exception
                r3 = r4
                goto L_0x003b
            L_0x0040:
                r3 = r4
                goto L_0x0036
            L_0x0042:
                r4 = r3
                goto L_0x0022
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Descriptors.EnumDescriptor.findValueByNumberCreatingIfUnknown(int):com.google.protobuf.Descriptors$EnumValueDescriptor");
        }

        /* access modifiers changed from: package-private */
        public int getUnknownEnumValueDescriptorCount() {
            return this.unknownValues.size();
        }

        private EnumDescriptor(DescriptorProtos.EnumDescriptorProto proto2, FileDescriptor file2, Descriptor parent, int index2) throws DescriptorValidationException {
            this.unknownValues = new WeakHashMap<>();
            this.index = index2;
            this.proto = proto2;
            this.fullName = Descriptors.computeFullName(file2, parent, proto2.getName());
            this.file = file2;
            this.containingType = parent;
            if (proto2.getValueCount() == 0) {
                throw new DescriptorValidationException(this, "Enums must contain at least one value.");
            }
            this.values = new EnumValueDescriptor[proto2.getValueCount()];
            for (int i = 0; i < proto2.getValueCount(); i++) {
                this.values[i] = new EnumValueDescriptor(proto2.getValue(i), file2, this, i);
            }
            file2.pool.addSymbol(this);
        }

        /* access modifiers changed from: private */
        public void setProto(DescriptorProtos.EnumDescriptorProto proto2) {
            this.proto = proto2;
            for (int i = 0; i < this.values.length; i++) {
                this.values[i].setProto(proto2.getValue(i));
            }
        }
    }

    public static final class EnumValueDescriptor extends GenericDescriptor implements Internal.EnumLite {
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private DescriptorProtos.EnumValueDescriptorProto proto;
        private final EnumDescriptor type;

        public int getIndex() {
            return this.index;
        }

        public DescriptorProtos.EnumValueDescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public int getNumber() {
            return this.proto.getNumber();
        }

        public String toString() {
            return this.proto.getName();
        }

        public String getFullName() {
            return this.fullName;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public EnumDescriptor getType() {
            return this.type;
        }

        public DescriptorProtos.EnumValueOptions getOptions() {
            return this.proto.getOptions();
        }

        private EnumValueDescriptor(DescriptorProtos.EnumValueDescriptorProto proto2, FileDescriptor file2, EnumDescriptor parent, int index2) throws DescriptorValidationException {
            this.index = index2;
            this.proto = proto2;
            this.file = file2;
            this.type = parent;
            this.fullName = parent.getFullName() + '.' + proto2.getName();
            file2.pool.addSymbol(this);
            file2.pool.addEnumValueByNumber(this);
        }

        private EnumValueDescriptor(FileDescriptor file2, EnumDescriptor parent, Integer number) {
            DescriptorProtos.EnumValueDescriptorProto proto2 = DescriptorProtos.EnumValueDescriptorProto.newBuilder().setName("UNKNOWN_ENUM_VALUE_" + parent.getName() + "_" + number).setNumber(number.intValue()).build();
            this.index = -1;
            this.proto = proto2;
            this.file = file2;
            this.type = parent;
            this.fullName = parent.getFullName() + '.' + proto2.getName();
        }

        /* access modifiers changed from: private */
        public void setProto(DescriptorProtos.EnumValueDescriptorProto proto2) {
            this.proto = proto2;
        }
    }

    public static final class ServiceDescriptor extends GenericDescriptor {
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private MethodDescriptor[] methods;
        private DescriptorProtos.ServiceDescriptorProto proto;

        public int getIndex() {
            return this.index;
        }

        public DescriptorProtos.ServiceDescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public String getFullName() {
            return this.fullName;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public DescriptorProtos.ServiceOptions getOptions() {
            return this.proto.getOptions();
        }

        public List<MethodDescriptor> getMethods() {
            return Collections.unmodifiableList(Arrays.asList(this.methods));
        }

        public MethodDescriptor findMethodByName(String name) {
            GenericDescriptor result = this.file.pool.findSymbol(this.fullName + '.' + name);
            if (result == null || !(result instanceof MethodDescriptor)) {
                return null;
            }
            return (MethodDescriptor) result;
        }

        private ServiceDescriptor(DescriptorProtos.ServiceDescriptorProto proto2, FileDescriptor file2, int index2) throws DescriptorValidationException {
            this.index = index2;
            this.proto = proto2;
            this.fullName = Descriptors.computeFullName(file2, null, proto2.getName());
            this.file = file2;
            this.methods = new MethodDescriptor[proto2.getMethodCount()];
            for (int i = 0; i < proto2.getMethodCount(); i++) {
                this.methods[i] = new MethodDescriptor(proto2.getMethod(i), file2, this, i);
            }
            file2.pool.addSymbol(this);
        }

        /* access modifiers changed from: private */
        public void crossLink() throws DescriptorValidationException {
            for (MethodDescriptor method : this.methods) {
                method.crossLink();
            }
        }

        /* access modifiers changed from: private */
        public void setProto(DescriptorProtos.ServiceDescriptorProto proto2) {
            this.proto = proto2;
            for (int i = 0; i < this.methods.length; i++) {
                this.methods[i].setProto(proto2.getMethod(i));
            }
        }
    }

    public static final class MethodDescriptor extends GenericDescriptor {
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private Descriptor inputType;
        private Descriptor outputType;
        private DescriptorProtos.MethodDescriptorProto proto;
        private final ServiceDescriptor service;

        public int getIndex() {
            return this.index;
        }

        public DescriptorProtos.MethodDescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public String getFullName() {
            return this.fullName;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public ServiceDescriptor getService() {
            return this.service;
        }

        public Descriptor getInputType() {
            return this.inputType;
        }

        public Descriptor getOutputType() {
            return this.outputType;
        }

        public DescriptorProtos.MethodOptions getOptions() {
            return this.proto.getOptions();
        }

        private MethodDescriptor(DescriptorProtos.MethodDescriptorProto proto2, FileDescriptor file2, ServiceDescriptor parent, int index2) throws DescriptorValidationException {
            this.index = index2;
            this.proto = proto2;
            this.file = file2;
            this.service = parent;
            this.fullName = parent.getFullName() + '.' + proto2.getName();
            file2.pool.addSymbol(this);
        }

        /* access modifiers changed from: private */
        public void crossLink() throws DescriptorValidationException {
            GenericDescriptor input = this.file.pool.lookupSymbol(this.proto.getInputType(), this, DescriptorPool.SearchFilter.TYPES_ONLY);
            if (!(input instanceof Descriptor)) {
                throw new DescriptorValidationException(this, ((char) Typography.quote) + this.proto.getInputType() + "\" is not a message type.");
            }
            this.inputType = (Descriptor) input;
            GenericDescriptor output = this.file.pool.lookupSymbol(this.proto.getOutputType(), this, DescriptorPool.SearchFilter.TYPES_ONLY);
            if (!(output instanceof Descriptor)) {
                throw new DescriptorValidationException(this, ((char) Typography.quote) + this.proto.getOutputType() + "\" is not a message type.");
            }
            this.outputType = (Descriptor) output;
        }

        /* access modifiers changed from: private */
        public void setProto(DescriptorProtos.MethodDescriptorProto proto2) {
            this.proto = proto2;
        }
    }

    /* access modifiers changed from: private */
    public static String computeFullName(FileDescriptor file, Descriptor parent, String name) {
        if (parent != null) {
            return parent.getFullName() + '.' + name;
        }
        String packageName = file.getPackage();
        if (!packageName.isEmpty()) {
            return packageName + '.' + name;
        }
        return name;
    }

    public static class DescriptorValidationException extends Exception {
        private static final long serialVersionUID = 5750205775490483148L;
        private final String description;
        private final String name;
        private final Message proto;

        public String getProblemSymbolName() {
            return this.name;
        }

        public Message getProblemProto() {
            return this.proto;
        }

        public String getDescription() {
            return this.description;
        }

        private DescriptorValidationException(GenericDescriptor problemDescriptor, String description2) {
            super(problemDescriptor.getFullName() + ": " + description2);
            this.name = problemDescriptor.getFullName();
            this.proto = problemDescriptor.toProto();
            this.description = description2;
        }

        private DescriptorValidationException(GenericDescriptor problemDescriptor, String description2, Throwable cause) {
            this(problemDescriptor, description2);
            initCause(cause);
        }

        private DescriptorValidationException(FileDescriptor problemDescriptor, String description2) {
            super(problemDescriptor.getName() + ": " + description2);
            this.name = problemDescriptor.getName();
            this.proto = problemDescriptor.toProto();
            this.description = description2;
        }
    }

    private static final class DescriptorPool {
        private boolean allowUnknownDependencies;
        private final Set<FileDescriptor> dependencies = new HashSet();
        private final Map<String, GenericDescriptor> descriptorsByName = new HashMap();
        /* access modifiers changed from: private */
        public final Map<DescriptorIntPair, EnumValueDescriptor> enumValuesByNumber = new HashMap();
        /* access modifiers changed from: private */
        public final Map<DescriptorIntPair, FieldDescriptor> fieldsByNumber = new HashMap();

        enum SearchFilter {
            TYPES_ONLY,
            AGGREGATES_ONLY,
            ALL_SYMBOLS
        }

        DescriptorPool(FileDescriptor[] dependencies2, boolean allowUnknownDependencies2) {
            this.allowUnknownDependencies = allowUnknownDependencies2;
            for (int i = 0; i < dependencies2.length; i++) {
                this.dependencies.add(dependencies2[i]);
                importPublicDependencies(dependencies2[i]);
            }
            for (FileDescriptor dependency : this.dependencies) {
                try {
                    addPackage(dependency.getPackage(), dependency);
                } catch (DescriptorValidationException e) {
                    throw new AssertionError(e);
                }
            }
        }

        private void importPublicDependencies(FileDescriptor file) {
            for (FileDescriptor dependency : file.getPublicDependencies()) {
                if (this.dependencies.add(dependency)) {
                    importPublicDependencies(dependency);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public GenericDescriptor findSymbol(String fullName) {
            return findSymbol(fullName, SearchFilter.ALL_SYMBOLS);
        }

        /* access modifiers changed from: package-private */
        public GenericDescriptor findSymbol(String fullName, SearchFilter filter) {
            GenericDescriptor result = this.descriptorsByName.get(fullName);
            if (result != null && (filter == SearchFilter.ALL_SYMBOLS || ((filter == SearchFilter.TYPES_ONLY && isType(result)) || (filter == SearchFilter.AGGREGATES_ONLY && isAggregate(result))))) {
                return result;
            }
            for (FileDescriptor dependency : this.dependencies) {
                result = dependency.pool.descriptorsByName.get(fullName);
                if (result != null && (filter == SearchFilter.ALL_SYMBOLS || ((filter == SearchFilter.TYPES_ONLY && isType(result)) || (filter == SearchFilter.AGGREGATES_ONLY && isAggregate(result))))) {
                    return result;
                }
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public boolean isType(GenericDescriptor descriptor) {
            return (descriptor instanceof Descriptor) || (descriptor instanceof EnumDescriptor);
        }

        /* access modifiers changed from: package-private */
        public boolean isAggregate(GenericDescriptor descriptor) {
            return (descriptor instanceof Descriptor) || (descriptor instanceof EnumDescriptor) || (descriptor instanceof PackageDescriptor) || (descriptor instanceof ServiceDescriptor);
        }

        /* access modifiers changed from: package-private */
        public GenericDescriptor lookupSymbol(String name, GenericDescriptor relativeTo, SearchFilter filter) throws DescriptorValidationException {
            String firstPart;
            String fullname;
            GenericDescriptor result;
            if (name.startsWith(".")) {
                fullname = name.substring(1);
                result = findSymbol(fullname, filter);
            } else {
                int firstPartLength = name.indexOf(46);
                if (firstPartLength == -1) {
                    firstPart = name;
                } else {
                    firstPart = name.substring(0, firstPartLength);
                }
                StringBuilder scopeToTry = new StringBuilder(relativeTo.getFullName());
                while (true) {
                    int dotpos = scopeToTry.lastIndexOf(".");
                    if (dotpos == -1) {
                        fullname = name;
                        result = findSymbol(name, filter);
                        break;
                    }
                    scopeToTry.setLength(dotpos + 1);
                    scopeToTry.append(firstPart);
                    result = findSymbol(scopeToTry.toString(), SearchFilter.AGGREGATES_ONLY);
                    if (result != null) {
                        if (firstPartLength != -1) {
                            scopeToTry.setLength(dotpos + 1);
                            scopeToTry.append(name);
                            result = findSymbol(scopeToTry.toString(), filter);
                        }
                        fullname = scopeToTry.toString();
                    } else {
                        scopeToTry.setLength(dotpos);
                    }
                }
            }
            if (result != null) {
                return result;
            }
            if (!this.allowUnknownDependencies || filter != SearchFilter.TYPES_ONLY) {
                throw new DescriptorValidationException(relativeTo, ((char) Typography.quote) + name + "\" is not defined.");
            }
            Descriptors.logger.warning("The descriptor for message type \"" + name + "\" can not be found and a placeholder is created for it");
            Descriptor descriptor = new Descriptor(fullname);
            this.dependencies.add(descriptor.getFile());
            return descriptor;
        }

        /* access modifiers changed from: package-private */
        public void addSymbol(GenericDescriptor descriptor) throws DescriptorValidationException {
            validateSymbolName(descriptor);
            String fullName = descriptor.getFullName();
            GenericDescriptor old = this.descriptorsByName.put(fullName, descriptor);
            if (old != null) {
                this.descriptorsByName.put(fullName, old);
                if (descriptor.getFile() == old.getFile()) {
                    int dotpos = fullName.lastIndexOf(46);
                    if (dotpos == -1) {
                        throw new DescriptorValidationException(descriptor, ((char) Typography.quote) + fullName + "\" is already defined.");
                    }
                    throw new DescriptorValidationException(descriptor, ((char) Typography.quote) + fullName.substring(dotpos + 1) + "\" is already defined in \"" + fullName.substring(0, dotpos) + "\".");
                }
                throw new DescriptorValidationException(descriptor, ((char) Typography.quote) + fullName + "\" is already defined in file \"" + old.getFile().getName() + "\".");
            }
        }

        private static final class PackageDescriptor extends GenericDescriptor {
            private final FileDescriptor file;
            private final String fullName;
            private final String name;

            public Message toProto() {
                return this.file.toProto();
            }

            public String getName() {
                return this.name;
            }

            public String getFullName() {
                return this.fullName;
            }

            public FileDescriptor getFile() {
                return this.file;
            }

            PackageDescriptor(String name2, String fullName2, FileDescriptor file2) {
                this.file = file2;
                this.fullName = fullName2;
                this.name = name2;
            }
        }

        /* access modifiers changed from: package-private */
        public void addPackage(String fullName, FileDescriptor file) throws DescriptorValidationException {
            String name;
            int dotpos = fullName.lastIndexOf(46);
            if (dotpos == -1) {
                name = fullName;
            } else {
                addPackage(fullName.substring(0, dotpos), file);
                name = fullName.substring(dotpos + 1);
            }
            GenericDescriptor old = this.descriptorsByName.put(fullName, new PackageDescriptor(name, fullName, file));
            if (old != null) {
                this.descriptorsByName.put(fullName, old);
                if (!(old instanceof PackageDescriptor)) {
                    throw new DescriptorValidationException(file, ((char) Typography.quote) + name + "\" is already defined (as something other than a package) in file \"" + old.getFile().getName() + "\".");
                }
            }
        }

        private static final class DescriptorIntPair {
            private final GenericDescriptor descriptor;
            private final int number;

            DescriptorIntPair(GenericDescriptor descriptor2, int number2) {
                this.descriptor = descriptor2;
                this.number = number2;
            }

            public int hashCode() {
                return (this.descriptor.hashCode() * 65535) + this.number;
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof DescriptorIntPair)) {
                    return false;
                }
                DescriptorIntPair other = (DescriptorIntPair) obj;
                if (this.descriptor == other.descriptor && this.number == other.number) {
                    return true;
                }
                return false;
            }
        }

        /* access modifiers changed from: package-private */
        public void addFieldByNumber(FieldDescriptor field) throws DescriptorValidationException {
            DescriptorIntPair key = new DescriptorIntPair(field.getContainingType(), field.getNumber());
            FieldDescriptor old = this.fieldsByNumber.put(key, field);
            if (old != null) {
                this.fieldsByNumber.put(key, old);
                throw new DescriptorValidationException(field, "Field number " + field.getNumber() + " has already been used in \"" + field.getContainingType().getFullName() + "\" by field \"" + old.getName() + "\".");
            }
        }

        /* access modifiers changed from: package-private */
        public void addEnumValueByNumber(EnumValueDescriptor value) {
            DescriptorIntPair key = new DescriptorIntPair(value.getType(), value.getNumber());
            EnumValueDescriptor old = this.enumValuesByNumber.put(key, value);
            if (old != null) {
                this.enumValuesByNumber.put(key, old);
            }
        }

        static void validateSymbolName(GenericDescriptor descriptor) throws DescriptorValidationException {
            String name = descriptor.getName();
            if (name.length() == 0) {
                throw new DescriptorValidationException(descriptor, "Missing name.");
            }
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if (('a' > c || c > 'z') && (('A' > c || c > 'Z') && c != '_' && ('0' > c || c > '9' || i <= 0))) {
                    throw new DescriptorValidationException(descriptor, ((char) Typography.quote) + name + "\" is not a valid identifier.");
                }
            }
        }
    }

    public static final class OneofDescriptor {
        private Descriptor containingType;
        /* access modifiers changed from: private */
        public int fieldCount;
        /* access modifiers changed from: private */
        public FieldDescriptor[] fields;
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private DescriptorProtos.OneofDescriptorProto proto;

        static /* synthetic */ int access$1908(OneofDescriptor x0) {
            int i = x0.fieldCount;
            x0.fieldCount = i + 1;
            return i;
        }

        public int getIndex() {
            return this.index;
        }

        public String getName() {
            return this.proto.getName();
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public String getFullName() {
            return this.fullName;
        }

        public Descriptor getContainingType() {
            return this.containingType;
        }

        public int getFieldCount() {
            return this.fieldCount;
        }

        public DescriptorProtos.OneofOptions getOptions() {
            return this.proto.getOptions();
        }

        public List<FieldDescriptor> getFields() {
            return Collections.unmodifiableList(Arrays.asList(this.fields));
        }

        public FieldDescriptor getField(int index2) {
            return this.fields[index2];
        }

        /* access modifiers changed from: private */
        public void setProto(DescriptorProtos.OneofDescriptorProto proto2) {
            this.proto = proto2;
        }

        private OneofDescriptor(DescriptorProtos.OneofDescriptorProto proto2, FileDescriptor file2, Descriptor parent, int index2) throws DescriptorValidationException {
            this.proto = proto2;
            this.fullName = Descriptors.computeFullName(file2, parent, proto2.getName());
            this.file = file2;
            this.index = index2;
            this.containingType = parent;
            this.fieldCount = 0;
        }
    }
}
